package one.wangwei.blockchain;

import one.wangwei.blockchain.block.Block;
import one.wangwei.blockchain.block.Blockchain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 测试
 *
 * @author wangwei
 * @date 2018/02/05
 */
public class BlockchainTest {

    public static void main(String[] args) {

        Blockchain blockchain = Blockchain.newBlockchain();
        blockchain.addBlock("Send 1 BTC to Ivan");
        blockchain.addBlock("Send 2 more BTC to Ivan");

        for (Block block : blockchain.getBlockList()) {
            System.out.println("Prev. hash: " + block.getPrevBlockHash());
            System.out.println("Data: " + block.getData());
            System.out.println("Date:" + LocalDateTime.ofInstant(Instant.ofEpochMilli(block.getTimeStamp()), ZoneId.systemDefault()));
            System.out.println("Hash: " + block.getHash());
            System.out.println();
        }
    }

}
