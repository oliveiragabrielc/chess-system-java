package boardgame;

public class Position {
    private Integer row;
    private Integer column;

    public Position(Integer row, Integer column) {
        this.row = row;
        this.column = column;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setPosition(int row, int column){
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString(){
        return "Row: "+ row + " Column: " + column;
    }
}
