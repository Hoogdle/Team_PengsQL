package com.example.vept.pl.L4.Compiler;

import android.util.Log;

import com.example.vept.pl.L4.PlannerDiagramViewModel;

public class PlannerCompiler {
    private final PlannerDiagramViewModel viewModel;
    private final PlannerLexer Lex;
    private final PlannerParser Pars;
    private final String[] TypeList = {
            "INTEGER",
            "INT",
            "int",
            "Int",
            "TEXT",
            "Text",
            "text",
            "REAL",
            "Real",
            "real",
            "NULL",
            "Null",
            "null",
            "BLOB",
            "Blob",
            "blob"
    };
    String Code;
    public PlannerCompiler(PlannerDiagramViewModel viewModel) {
        this.viewModel = viewModel;
        Lex = new PlannerLexer(this);
        Lex.SetTypes(TypeList);
        Pars = new PlannerParser(viewModel, Lex);
        Pars.SetTypes(TypeList);
    }

    public void Compile(String Code) {
        this.Code = Code;
        Lex.SetCode(this.Code);
        Pars.Start();
    }
}
