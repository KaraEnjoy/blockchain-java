package one.wangwei.blockchain;

import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;
import one.wangwei.blockchain.cli.CLI;
import one.wangwei.blockchain.pow.ProofOfWork;
import org.junit.Test;

/**
 * 测试
 *
 * @author wangwei
 * @date 2018/02/05
 */
public class BlockchainTest {


    @Test
    public void test1() throws Exception {

        Blockchain blockchain = Blockchain.newBlockchain();

        blockchain.addBlock("Send 1 BTC to Ivan");
        blockchain.addBlock("Send 2 more BTC to Ivan");

        Blockchain.BlockchainIterator blockchainIterator = blockchain.getBlockchainIterator();
        while (blockchainIterator.hashNext()){
            Block block = blockchainIterator.next();
            System.out.println("Prev.hash: " + block.getPrevBlockHash());
            System.out.println("Data: " + block.getData());
            System.out.println("Hash: " + block.getHash());
            System.out.println("Nonce: " + block.getNonce());
            ProofOfWork pow = ProofOfWork.newProofOfWork(block);
            System.out.println("Pow valid: " + pow.validate() + "\n");
        }


    }

    public static void main(String[] args) {
        try {
//            String argss[] = {"-addblock", "Send 2.0 BTC to wangwei"};
            String argss[] = {"-print"};
            CLI cli = new CLI(argss);
            cli.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
