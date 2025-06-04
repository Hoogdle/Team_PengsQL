package com.example.vept.pl.L4.Compiler;

import com.example.vept.pl.L4.PlannerDiagramViewModel;

import java.security.Key;
import java.util.Arrays;
import java.util.stream.Stream;

public class PlannerLexer {
    private final PlannerCompiler compiler;
    char[] Code;
    int line, col, ind, state;
    private String[] Keywordlis = {
            "PK",
            "NN",
            "UQ",
            "DF",
            "table"
    };
    public PlannerLexer(PlannerCompiler compiler) {
        this.compiler = compiler;
        Arrays.sort(Keywordlis);
    }

    public void SetTypes(String[] types) {
        Keywordlis = Stream.of(Keywordlis, types).flatMap(Stream::of).toArray(String[]::new);
        Arrays.sort(Keywordlis);
    }

    public void SetCode(String Code) {
        this.Code = Code.toCharArray();
        line = 1;
        col = 0;
        ind = 0;
        state = 0;
    }

    public int GetLine() {
        return line;
    }

    public LexerStruct gettoken() {
        int state = 0;
        char c = ' ';
        LexerStruct ret = new LexerStruct();
        ret.Text = "";
        while (state != -1) {
            col += 1;
            if(Code.length > ind)
                c = Code[ind];
            else { ret.Type = TokenType.EOF; state = -1;}
            ind += 1;
            switch (state)
            {
                case 0:
                {
                    if (c == ' ') continue;
                    if (c == '\n') {
                        line += 1;
                        col = 0;
                        continue;
                    }
                    if (Character.isLetter(c)) {
                        state = 1;
                        ret.Text += c;
                    }
                    if (c == '{' || c == '}' || c == '(' || c == ')' || c == ',' || c == '.' || c == '=') {
                        ret.Text += c;
                        state = -1;
                        ret.Type = TokenType.OPERATOR;
                    }

                    if (c == '"') {
                        state = 2;
                        ret.Text += c;
                    }

                    break;
                }
                case 1:
                {
                    if (Character.isLetter(c) || Character.isDigit(c)) {
                        state = 1;
                        ret.Text += c;
                    }
                    else {
                        ind -= 1;
                        state = -1;
                        if (isKeyword(ret.Text)) ret.Type = TokenType.KEYWORD;
                        else ret.Type = TokenType.ID;
                    }
                    break;
                }
                case 2:
                {
                    if (c != '"') {
                        state = 2;
                        ret.Text += c;
                    }
                    else {
                        state = -1;
                        ret.Type = TokenType.LITE;
                    }
                    break;
                }
                default:
                    break;
            }

        }
        return ret;
    }

    private boolean isKeyword(String Text) {
        int i = 0, j = Keywordlis.length - 1;
        int center;
        while(i <= j) {
            center = (i + j) / 2;
            int temp = Text.compareTo(Keywordlis[center]);
            if(temp == 0) return true;
            else if(temp > 0) i = center + 1;
            else j = center - 1;
        }

        return false;
    }
}
