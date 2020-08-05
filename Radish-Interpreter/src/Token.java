
public abstract class Token {
    public static final Token EOF=new Token(-1){};
    public static final String EOL="\\n";
    private int lineNumber;

    protected Token(int line){
        lineNumber=line;
    }

    public int getLineNumber(){return lineNumber;}
    public boolean isIdentifier(){return false;}
    public boolean isNumber(){return false;}
    public int getNumber(){throw new RuntimeException("not number token");}
    public String getText(){return "";}

    //REs for identifiers:
    /*
        RE for identifiers:
        ([a-z_A-Z][a-z_A-Z0-9]*|>|>=|<|<=|==|&&|\|\|)

        RE for number literal:
        [1-9][0-9]*

        RE for string literal:
        "(\\"|\\\\|\\n|[^"])*"
     */

}
