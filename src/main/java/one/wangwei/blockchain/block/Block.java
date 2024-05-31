package one.wangwei.blockchain.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import one.wangwei.blockchain.pow.PowResult;
import one.wangwei.blockchain.pow.ProofOfWork;
import one.wangwei.blockchain.transaction.Transaction;
import one.wangwei.blockchain.util.ByteUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Instant;

/**
 * 区块
 *
 * @author wangwei
 * @date 2018/02/02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Block {

    /**
     * 区块hash值
     */
    private String hash;
    /**
     * 前一个区块的hash值
     */
    private String prevBlockHash;
    /**
     * 交易信息， 也就是每个 block 的数据区域
     * 从现在开始，每一个区块必须存储至少一个交易信息，并且尽可能地避免在没有交易数据的情况下进行挖矿。
     */
    private Transaction[] transactions;
    /**
     * 区块创建时间(单位:秒)
     */
    private long timeStamp;
    /**
     * 工作量证明计数器
     */
    private long nonce;


    /**
     * <p> 创建创世区块 </p>
     *
     * @param coinbase
     * @return
     */
    public static Block newGenesisBlock(Transaction coinbase) {
        return Block.newBlock(ByteUtils.ZERO_HASH, new Transaction[]{coinbase});
    }

    /**
     * <p> 创建新区块 </p>
     *
     * @param previousHash
     * @param transactions
     * @return
     */
    public static Block newBlock(String previousHash, Transaction[] transactions) {
        Block block = new Block("", previousHash, transactions, Instant.now().getEpochSecond(), 0);
        ProofOfWork pow = ProofOfWork.newProofOfWork(block);
        PowResult powResult = pow.run();
        block.setHash(powResult.getHash());
        block.setNonce(powResult.getNonce());
        return block;
    }

    /**
     * 对区块中的交易信息进行Hash计算
     * 我们使用哈希值来作为数据的唯一标识。
     * 我们希望区块中的所有交易数据都能通过一个哈希值来定义它的唯一标识。
     * 为了达到这个目的，我们计算了每一个交易的唯一哈希值，然后将他们串联起来，再对这个串联后的组合进行哈希值计算。
     *
     * <p>
     * 比特币使用更复杂的技术：它将所有包含在块中的交易表示为 Merkle 树 ，
     * 并在 Proof-of-Work 系统中使用该树的根散列。 这种方法只需要跟节点的散列值就可以快速检查块是否包含某笔交易，而无需下载所有交易。
     *
     * @return
     */
    public byte[] hashTransaction() {
        byte[][] txIdArrays = new byte[this.getTransactions().length][];
        for (int i = 0; i < this.getTransactions().length; i++) {
            txIdArrays[i] = this.getTransactions()[i].getTxId();
        }
        return DigestUtils.sha256(ByteUtils.merge(txIdArrays));
    }
}
