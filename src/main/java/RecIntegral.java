import java.util.ArrayList;
import java.util.List;

public class RecIntegral {
    private List<String> record;

    public List<String> getRecord() {
        return record;
    }
    public void setRecord(List<String> record) {
        this.record = record;
    }
    public void setDataByIndex(int index, String record) {
        this.record.set(index, record);
    }
    public RecIntegral(List<String> record){
        this.record = new ArrayList<>(record);
    }
}