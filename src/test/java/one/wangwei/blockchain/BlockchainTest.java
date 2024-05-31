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
