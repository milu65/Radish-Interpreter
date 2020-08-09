import java.io.IOException;
import java.io.Reader;

public class InputReader extends Reader {

    private String buffer=null;
    private int pos=0;
    public InputReader(){
        super();
        buffer="if 1<2{return 0}";
    }
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if(buffer==null)return -1;
        if(pos>=buffer.length())return -1;
        int size;
        for(size=0;size<len&&pos<buffer.length();){
            cbuf[off+size++]=buffer.charAt(pos++);
        }
        return size;
    }

    @Override
    public void close() throws IOException {

    }
}
