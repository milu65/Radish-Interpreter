import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    public static String regexPat
            = "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
            + "|[A-Za-z][A-Za-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
    public Pattern pattern=Pattern.compile(regexPat);

    private LineNumberReader reader;
    private boolean hasNext=true;

    private ArrayList<Token> queue=new ArrayList<>();

    public Lexer(Reader reader){
        this.reader=new LineNumberReader(reader);
    }

    public Token read() throws ParseException {
        if(queue.isEmpty())fillQueue(0);
        if(queue.isEmpty())return Token.EOF;
        Token ret=queue.remove(0);
        return ret;
    }

    public Token peek(int n) throws ParseException {
        if(queue.size()<=n)fillQueue(n);
        if(queue.size()<=n)return Token.EOF;
        return queue.get(n);
    }

    private void fillQueue(int n) throws ParseException {
        while(n>=queue.size()&&hasNext){
            readLine();
        }
    }

    private void readLine() throws ParseException {//review
        String line;
        try{
            line=reader.readLine();
        } catch (IOException e) {
            throw new ParseException();
        }
        if(line==null){
            hasNext=false;
            return;
        }
        int lineNo=reader.getLineNumber();
        Matcher matcher=pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int pos=0;
        int endPos=line.length();
        while(pos<endPos){
            matcher.region(pos,endPos);
            if(matcher.lookingAt()){
                addToken(lineNo,matcher);
                pos = matcher.end();
            }else
                throw new ParseException("mismatch token at line: "+lineNo);
        }
    }

    private void addToken(int lineNo, Matcher matcher) {//review
        String m = matcher.group(1);
        if (m != null) // if not a space
            if (matcher.group(2) == null) { // if not a comment
                Token token;
                if (matcher.group(3) != null)
                    token = new NumToken(lineNo, Integer.parseInt(m));
                else if (matcher.group(4) != null)
                    token = new StrToken(lineNo, toStringLiteral(m));
                else
                    token = new IdToken(lineNo, m);
                queue.add(token);
            }
    }
    protected String toStringLiteral(String s) {//review
        StringBuilder sb = new StringBuilder();
        int len = s.length() - 1;
        for (int i = 1; i < len; i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < len) {
                int c2 = s.charAt(i + 1);
                if (c2 == '"' || c2 == '\\')
                    c = s.charAt(++i);
                else if (c2 == 'n') {
                    ++i;
                    c = '\n';
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    class NumToken extends Token{
        private Integer value;
        public NumToken(int line,Integer value) {
            super(line);
            this.value=value;
        }

        @Override
        public boolean isNumber() {
            return true;
        }

        @Override
        public int getNumber() {
            return value;
        }

        @Override
        public String getText() {
            return String.valueOf(value);
        }
    }
    class StrToken extends Token{
        private String str;
        public StrToken(int line,String str){
            super(line);
            this.str=str;
        }
        public boolean isString(){
            return true;
        }

        @Override
        public String getText() {
            return str;
        }
    }
    class IdToken extends Token{
        private String id;
        public IdToken(int line,String id){
            super(line);
            this.id=id;
        }

        @Override
        public boolean isIdentifier() {
            return true;
        }

        @Override
        public String getText() {
            return id;
        }
    }
}
