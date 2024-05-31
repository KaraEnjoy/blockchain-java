package one.wangwei.blockchain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.util.SerializeUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * 交易
 *
 * @author wangwei
 * @date 2017/03/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    /**
     * SUBSIDY 是挖矿奖励数量。
     * 在比特币中，这个奖励数量没有存储在任何地方，而是依据现有区块的总数进行计算而得到：区块总数 除以 210000。开采创世区块得到的奖励为 50BTC，每过 210000 个区块，奖励会减半。
     * 在我们的实现中，我们暂且将挖矿奖励设置为常数。（至少目前是这样）
     */
    private static final int SUBSIDY = 10;

    /**
     * 交易的Hash
     */
    private byte[] txId;
    /**
     * 交易输入
     */
    private TXInput[] inputs;
    /**
     * 交易输出
     */
    private TXOutput[] outputs;


    /**
     * 设置交易ID
     */
    private void setTxId() {
        this.setTxId(DigestUtils.sha256(SerializeUtils.serialize(this)));
    }

    /**
     * 创建CoinBase交易
     *
     * @param to   收账的钱包地址
     * @param data 解锁脚本数据
     * @return
     */
    public static Transaction newCoinbaseTX(String to, String data) {
        if (StringUtils.isBlank(data)) {
            data = String.format("Reward to '%s'", to);
        }
        // 创建交易输入
        TXInput txInput = new TXInput(new byte[]{}, -1, data);
        // 创建交易输出
        TXOutput txOutput = new TXOutput(SUBSIDY, to);
        // 创建交易
        Transaction tx = new Transaction(null, new TXInput[]{txInput}, new TXOutput[]{txOutput});
        // 设置交易ID
        tx.setTxId();
        return tx;
    }

    /**
     * 是否为 Coinbase 交易
     *
     * @return
     */
    public boolean isCoinbase() {
        return this.getInputs().length == 1
                && this.getInputs()[0].getTxId().length == 0
                && this.getInputs()[0].getTxOutputIndex() == -1;
    }


    /**
     * 从 from 向  to 支付一定的 amount 的金额
     * 现在，我们想要给某人发送一些币。因此，我们需要创建一笔新的交易，然后放入区块中，再进行挖矿。
     *
     * @param from       支付钱包地址
     * @param to         收款钱包地址
     * @param amount     交易金额
     * @param blockchain 区块链
     * @return
     */
    public static Transaction newUTXOTransaction(String from, String to, int amount, Blockchain blockchain) throws Exception {
        // 找到地址中可花费的钱
        SpendableOutputResult result = blockchain.findSpendableOutputs(from, amount);
        int accumulated = result.getAccumulated();
        Map<String, int[]> unspentOuts = result.getUnspentOuts();

        // 余额对比转账金额
        if (accumulated < amount) {
            throw new Exception("ERROR: Not enough funds");
        }

        Iterator<Map.Entry<String, int[]>> iterator = unspentOuts.entrySet().iterator();

        // 交易中所有的 input
        TXInput[] txInputs = {};
        while (iterator.hasNext()) {
            Map.Entry<String, int[]> entry = iterator.next();
            String txIdStr = entry.getKey();
            int[] outIdxs = entry.getValue();
            byte[] txId = Hex.decodeHex(txIdStr);
            for (int outIndex : outIdxs) {
                txInputs = ArrayUtils.add(txInputs, new TXInput(txId, outIndex, from));
            }
        }

        // 交易中 ouput
        TXOutput[] txOutput = {};
        txOutput = ArrayUtils.add(txOutput, new TXOutput(amount, to));
        // 增加未花费的金额
        if (accumulated > amount) {
            txOutput = ArrayUtils.add(txOutput, new TXOutput((accumulated - amount), from));
        }

        // 创建新的 交易
        Transaction newTx = new Transaction(null, txInputs, txOutput);
        newTx.setTxId();
        return newTx;
    }
}
