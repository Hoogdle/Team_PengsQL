package com.pangsql.vept.pl.L4.Compiler;

import android.renderscript.Int4;

import com.pangsql.vept.pl.L4.Field;
import com.pangsql.vept.pl.L4.PlannerDiagramViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class PlannerParser {
    private boolean isError;
    private final PlannerLexer Lex;
    private LexerStruct LastToken;
    private String CurrTable;
    private String CurrField;
    private String[] RecoveryTexts = {
        "table"
    };
    private String[] TypeList = {
    };
    private boolean isAlive;
    private boolean canRecovery;
    private String RecoverPoint;
    private Map<String, DiagramMessage> TableList;
    private List<DiagramFK> FKeyList;
    private List<String> TableNameList;
    private final PlannerDiagramViewModel viewModel;
    private boolean isexpk;
    private boolean isNN;
    private boolean isDF;
    private boolean isUQ;
    private boolean isfkey;
    public PlannerParser(PlannerDiagramViewModel viewModel, PlannerLexer Lex) {
        this.Lex = Lex;
        this.CurrTable = "";
        this.viewModel = viewModel;
        isAlive = true;
        canRecovery = true;
        RecoverPoint = "";
        isexpk = false;
    }

    public void SetTypes(String[] types) {
        TypeList = Stream.of(TypeList, types).flatMap(Stream::of).toArray(String[]::new);
        RecoveryTexts = Stream.of(RecoveryTexts, types).flatMap(Stream::of).toArray(String[]::new);
    }

    private void match(String code) {
        if(isAlive) {
            if(LastToken.Text.equals(code)) LastToken = Lex.gettoken();
            else ShowError();
        }
    }

    private void matchtype(TokenType type) {
        if(isAlive) {
            if (LastToken.Type == type) LastToken = Lex.gettoken();
            else ShowError();
        }
    }

    private void ShowError() {
        isError = true;
        if(isAlive) {
            viewModel.adderror(Lex.GetLine() - 1);
        }

        isAlive = false;

        while(LastToken.Type != TokenType.EOF && Arrays.stream(RecoveryTexts).noneMatch(s -> s.equals(LastToken.Text))) {
            LastToken = Lex.gettoken();
        }
    }

    private void ShowRunError(String msg) {
        viewModel.adderror(2);
    }

    private void ShowRefError(int Line) {
        viewModel.adderror(Line - 1);
        isError = true;
    }

    public void Start() {
        isError = false;
        canRecovery = true;
        viewModel.ResetError();
        TableList = new HashMap<>();
        FKeyList = new ArrayList<>();
        TableNameList = new ArrayList<>();
        LastToken = Lex.gettoken();
        Table();
        while(canRecovery) {
            Recover();
        }
        Dosemantics();
    }

    private void Dosemantics() {
        String EMsg = "";
        AtomicInteger LastLine = new AtomicInteger(-1);
        AtomicInteger xoff = new AtomicInteger(0);
        AtomicInteger yoff = new AtomicInteger(0);
        List<Int4> RefLines = new ArrayList<>();
        if(!isError) {
            try {
                FKeyList.forEach((k) -> {
                    DiagramMessage tmpTb = TableList.get(k.FromTable);
                    DiagramMessage tmpTbt = TableList.get(k.ToTable);
                    LastLine.set(k.CodeLine);
                    int FromDia = tmpTb.Index;
                    int ToDia = tmpTbt.Index;
                    int FromField = Integer.parseInt(tmpTb.Fields.get(k.FromField).get("Index"));
                    int ToField = Integer.parseInt(tmpTbt.Fields.get(k.ToField).get("Index"));
                    if(!tmpTbt.Fields.get(k.ToField).get("PK").equals("1")) ShowRefError(LastLine.get());
                    RefLines.add(new Int4(FromDia,ToDia,FromField,ToField));
                });
            } catch (Exception e) {
                ShowRefError(LastLine.get());
            }
        }
        if(!isError) {
            try {
                viewModel.ResetDiagram();
                TableNameList.forEach((s) ->
                {
                    DiagramMessage dm = TableList.get(s);
                    Map<String,Map<String,String>> TField = dm.Fields;
                    Field[] FList = new Field[TField.size()];
                    dm.Fields.forEach((fs, op) -> {
                        boolean ispk = op.containsKey("PK") ? true : false;
                        boolean isnn = op.containsKey("NN") ? true : false;
                        FList[Integer.parseInt(op.get("Index"))] = new Field(fs, op.get("Type"), ispk, isnn, 0);
                    });
                    viewModel.addDiagram(s,xoff.get(),xoff.get(), Arrays.asList(FList));
                    xoff.set(xoff.get() + 20);
                    yoff.set(yoff.get() + 20);
                });
            } catch (Exception e) {
                ShowRunError(EMsg);
            }
        }
        if(!isError) {
            try {
                    RefLines.forEach((it) -> {
                        viewModel.AddForienKey(it.x,it.y,it.z,it.w);
                    });
            } catch (Exception e) {

            }
        }
    }
    public void Recover() {
        if(LastToken.Text.equals("table")) {
            RecoverPoint = LastToken.Text;
        } else if(LastToken.Type == TokenType.EOF) {
            canRecovery = false;
        } else if(Arrays.stream(TypeList).anyMatch(s -> s.equals(LastToken.Text))) {
            RecoverPoint = LastToken.Text;
        }
        if(LastToken.Type != TokenType.EOF)
            Table();
    }
    private void Table() {
        if(LastToken.Text.equals("table") || Arrays.stream(TypeList).anyMatch(s -> s.equals(LastToken.Text))) {
            if(Arrays.stream(TypeList).noneMatch(s -> s.equals(LastToken.Text))) {
                isAlive = true;
                match("table");
                CurrTable = LastToken.Text;
                Id();
                if(TableList.containsKey(CurrTable)) ShowError();
                else {
                    DiagramMessage tmp = new DiagramMessage();
                    tmp.Fields = new HashMap<>();
                    tmp.Index = TableList.size();
                    TableList.put(CurrTable, tmp);
                    TableNameList.add(CurrTable);
                    isexpk = false;
                }
                match("{");
            }
            Field();
            if(isAlive) Table();
        } else if(LastToken.Type == TokenType.EOF) {
            canRecovery = false;
        } else {
            ShowError();
        }
    }
    private void Field() {
        String CurrType;
        if(!LastToken.Text.equals("}")) {
            CurrType = LastToken.Text;
            isAlive = true;
            Type();
            CurrField = LastToken.Text;
            Id();
            try {
                DiagramMessage tmp = TableList.get(CurrTable);
                if(tmp != null && !tmp.Fields.containsKey(CurrField)) {
                    Map<String, String> options = new HashMap<>();
                    options.put("Type", CurrType);
                    options.put("Index", String.valueOf(tmp.Fields.size()));
                    tmp.Fields.put(CurrField, options);
                    isfkey = false;
                    isNN = false;
                    isDF = false;
                    isUQ = false;
                } else {
                    ShowError();
                }
            } catch (Exception e) {
                ShowError();
            }
            match("(");
            FKey();
            match("{");
            SConsts();
            if(isAlive) Field();
        } else {
            match("}");
            CurrTable = "";
        }
    }
    private void FKey() {
        String preTb;
        String preFd;
        if(LastToken.Text.equals(")")) {
            match(")");
        } else {
            preTb = LastToken.Text;
            Id();
            match(".");
            preFd = LastToken.Text;
            Id();
            DiagramFK tmp = new DiagramFK();
            tmp.FromTable = CurrTable;
            tmp.ToTable = preTb;
            tmp.FromField = CurrField;
            tmp.ToField = preFd;
            tmp.CodeLine = Lex.GetLine();
            FKeyList.add(tmp);
            if(isAlive) isfkey = true;
            match(")");
        }
    }
    private void Type() {
        boolean isOk = false;
        for(String l : TypeList) {
            if(LastToken.Text.equals(l)) {
                match(l);
                isOk = true;
                break;
            }
        }

        if(!isOk) {
            ShowError();
        }
    }
    private void SConsts() {
        if(!LastToken.Text.equals("}")) {
            Const();
            Consts();
        } else {
            match("}");
        }
    }
    private void Consts() {
        if(LastToken.Text.equals(",")) {
            match(",");
            Const();
            if(isAlive) Consts();
        } else {
            match("}");
        }
    }
    private void Const() {
        String preToken;
        if(LastToken.Text.equals("PK")) {
            if(!isexpk && !isfkey) {
                isexpk = true;
                match("PK");
                SetConst("PK", "1");
            } else {
                ShowError();
            }
        } else if(LastToken.Text.equals("NN")) {
            if(!isNN) {
                isNN = true;
                match("NN");
                SetConst("NN", "1");
            }
        } else if(LastToken.Text.equals("DF")) {
            if(!isDF && !isfkey) {
                isDF = true;
                match("DF");
                match("=");
                preToken = LastToken.Text;
                matchtype(TokenType.LITE);
                SetConst("DF", preToken);
            }
        } else if(LastToken.Text.equals("UQ")) {
            if(!isUQ && !isfkey) {
                isUQ = true;
                match("UQ");
                SetConst("UQ", "1");
            }
        } else {
            ShowError();
        }
    }

    private void SetConst(String con, String value) {
        try {
            DiagramMessage tmp = TableList.get(CurrTable);
            if(tmp != null && tmp.Fields.containsKey(CurrField)) {
                tmp.Fields.get(CurrField).put(con, value);
            } else {
                ShowError();
            }
        } catch (Exception e) {
            ShowError();
        }
    }
    private void Id() {
        matchtype(TokenType.ID);
    }
}
