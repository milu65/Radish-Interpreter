import java.io.Reader;

public class LexerRunner {
    public static void main(String[] args) throws ParseException {
        Reader reader=new InputReader();
        Lexer lexer=new Lexer(reader);
        for(Token token; (token=lexer.read())!=Token.EOF;){
            System.out.println(token.getText());
        }
    }
}
