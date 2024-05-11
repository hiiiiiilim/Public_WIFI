package db;

import java.util.List;

public class HistoryData {
    private String history_id;
    private String x;
    private String y;
    private String search_DT;
   
	public HistoryData(String history_id, String x, String y, String search_DT) {
		super();
		this.history_id = history_id;
		this.x = x;
		this.y = y;
		this.search_DT = search_DT;
	}
	public String getHistory_id() {
		return history_id;
	}
	public String getX() {
		return x;
	}
	public String getY() {
		return y;
	}
	public String getSearch_DT() {
		return search_DT;
	}
	public void setHistory_id(String history_id) {
		this.history_id = history_id;
	}
	public void setX(String x) {
		this.x = x;
	}
	public void setY(String y) {
		this.y = y;
	}
	public void setSearch_DT(String search_DT) {
		this.search_DT = search_DT;
	}
    

}
