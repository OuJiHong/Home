

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.cqjr.web.domain.fcparam.FcTransferInfoDO;
import com.cqjr.web.domain.trade.OrderDO;
import com.cqjr.web.interest.trusteeship.TransSubmitOJH;
import com.cqjr.web.interest.trusteeship.entity.AddBidInfoRequest;
import com.cqjr.web.interest.trusteeship.entity.AddBidInfoResult;
import com.cqjr.web.interest.trusteeship.entity.AutoTenderCancleRequest;
import com.cqjr.web.interest.trusteeship.entity.AutoTenderCancleResult;
import com.cqjr.web.interest.trusteeship.entity.AutoTenderCancleRequest.IsUnFreeze;

/**
 * 资金托管 接口测试
 * @author OJH
 *
 */
public class TransTest {

	
	/**
	 * test
	 * @param args
	 */
	public static void main_addBid(String[] args) {
		
		AddBidInfoRequest addBidInfoRequest = new AddBidInfoRequest();
		addBidInfoRequest.setProId("2017102111201203");//16位
//		addBidInfoRequest.setBorrCustId("6000060007339794");
		addBidInfoRequest.setBorrCustId("6000060007791330");
		addBidInfoRequest.setBorrTotAmt(new BigDecimal("100000"));
		addBidInfoRequest.setYearRate(0.1);
		addBidInfoRequest.setRetType("04");
		Date startDate = new Date();
		Date endDate = DateUtils.addDays(startDate, 3);
		Date retDate = DateUtils.addDays(endDate, 5);
		addBidInfoRequest.setBidStartDate(startDate);
		addBidInfoRequest.setBidEndDate(endDate);
		addBidInfoRequest.setRetAmt(new BigDecimal("120000"));
		addBidInfoRequest.setRetDate(retDate);
		addBidInfoRequest.setProArea("3100");
		addBidInfoRequest.setBgRetUrl(TransSubmitOJH.BG_RET_URL);
		
		AddBidInfoResult result = null;
		
		try {
//			result = addBidInfoRequest.execute();
//			System.out.println("verifySign:" + result.verifySign());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		System.out.println(result);
		
	}
	
	
	public static void main(String[] args) {
//		AutoTenderCancleRequest request = new AutoTenderCancleRequest();
//		request.setOrdId("201710261059");
//		request.setOrdDate(new Date());
//		request.setTransAmt(new BigDecimal("20"));
//		request.setUsrCustId("6000060007783241");
//		request.setIsUnFreeze(IsUnFreeze.N);
//		request.setBgRetUrl(TransSubmitOJH.BG_RET_URL);
		
		
		TransSubmitOJH transSubmitOJH = new  TransSubmitOJH();
		
		OrderDO orderDo = new OrderDO();
		orderDo.setOrderId("20171025111803833821");
		try {
			orderDo.setCreateDate(DateUtils.parseDate("2017-10-25 11:18:43", new String[]{"yyyy-MM-dd HH:mm:ss"} ));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		orderDo.setPriceAmount(30000L);
		FcTransferInfoDO fcTransferInfoDO = new FcTransferInfoDO();
		fcTransferInfoDO.setInfoContent("{UsrCustId=6000060007783241, TransAmt=300.00, FeeCustId=, BgRetUrl=http://180.168.191.42:1563/openapi/backHuifu/backInfoOperate.htm, ActuAmt=, SecRespDesc=, FreezeOrdId=20171025111803884448, SecRespCode=, RetUrl=http://192.168.88.156:8889/huifuReturnPage.htm, RespExt=, MerPriv=YTRhMGYwZGUtODQxMC00YTQ1LWI0YjMtMDBkZmQzMzhiYzE3, TrxId=201710250005200224, FeeAcctId=, RespCode=000, FeeAmt=, FreezeTrxId=201710250011254473, OrdDate=20171025, RespDesc=成功, ChkValue=0D41919EB8DBC6710F4A542513A2F71D2750A4A67A817AB090D953BBE3A8063A42F5C62F1E909D92F128381CB7D39410553AA1B3322D10045EA3D7665646CE244F8CF0B0E5EC7ADD9005A2C12297C7461E006A167D0554ECEDEA584B5625C9B8C4CC0CD1CF777141800C23E197F8A956C4143666506045CFAAE09159578AE460, MerCustId=6000060007339794, OrdId=20171025111803833821, CmdId=InitiativeTender, IsFreeze=Y, DepoBankSeq=}");
		fcTransferInfoDO.setInfoContentType("map");
		try {
			transSubmitOJH.autoTenderCancle(orderDo, fcTransferInfoDO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
