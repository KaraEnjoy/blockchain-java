package one.wangwei.blockchain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易输入
 *
 * @author wangwei
 * @date 2017/03/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TXInput {

    /**
     * 交易Id的hash值
     */
    private byte[] txId;
    /**
     * 交易输出索引，存储的是交易中这个交易输出的索引位置（因为一笔交易可能包含多个交易输出）
     */
    private int txOutputIndex;
    /**
     * 解锁脚本：主要是提供用于交易输出中 ScriptPubKey 所需的验证数据。
     * 1. 如果这个数据被验证正确，那么相应的交易输出将被解锁，并且其中的 value 能够生成新的交易输出；
     * 2. 如果不正确，那么相应的交易输出将不能被交易输入所引用；
     */
    private String scriptSig;


    /**
     * 判断解锁数据是否能够解锁交易输出
     *
     * @param unlockingData
     * @return
     */
    public boolean canUnlockOutputWith(String unlockingData) {
        return this.getScriptSig().endsWith(unlockingData);
    }

}
