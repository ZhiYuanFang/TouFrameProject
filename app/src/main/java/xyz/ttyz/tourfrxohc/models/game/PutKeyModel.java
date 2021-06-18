package xyz.ttyz.tourfrxohc.models.game;

/**
 * 投放钥匙的模型
 * */
public class PutKeyModel {
    int putNumber;//0 表示查看所有  1~3 表示往该格子投放

    public int getPutNumber() {
        return putNumber;
    }

    public void setPutNumber(int putNumber) {
        this.putNumber = putNumber;
    }
}
