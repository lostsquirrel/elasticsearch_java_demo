package test.lisong.elastic.client;

import java.util.Date;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

public class TestBuilk extends BaseTest {

	@Test
	public void testBulk() throws Exception {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (int i = 0; i < data.length; i++) {
			bulkRequest.add(client.prepareIndex("twitter", "tweet", ""+(1000 + i))
			        .setSource(XContentFactory.jsonBuilder()
			                    .startObject()
			                        .field("user", data[i].split("@").length >= 2 ? data[i].split("@")[1] : "无名氏")
			                        .field("postDate", new Date())
			                        .field("message", data[i])
			                    .endObject()
			                  )
			        );
		}
		
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
		    // process failures by iterating through each bulk response item
			BulkItemResponse[] items = bulkResponse.getItems();
			for (BulkItemResponse item : items) {
				System.out.println(item.getFailureMessage());
			}
		}
		
	}
	
	private String[] data = {
			"没有银弹(万能药)	NO silver bullet	@Fred Brooks (图灵奖得主 &#12298;人月神话&#12299;作者)  http://program-think.blogspot.com/2009/03/book-review-mythical-man-month.html",
			"编程的艺术就是处理复杂性的艺术	@Edsger Dijkstra (图灵奖得主)",

			"简单即是美	Simple is beautiful",
			"简单是可靠的先决条件	Simplicity is prerequisite for reliability.	@Edsger Dijkstra (图灵奖得主)",
			"优秀软件的作用是让复杂的东西看起来简单	@Grady Booch (UML创始人之一)",
			"设计软件有两种方法: 一种是简单到极致而明显没有缺陷; 另一种是复杂到极致以至于没有明显的缺陷&#12290;前者要难得多!	@C.A.R.Hoare",

			"优秀的设计在不断地演化	糟糕的设计在不断地打补丁",
			"最纯粹&#12289;最抽象的设计难题就是设计桥梁&#12290;你面对的问题是: 如何用最少的材料, 跨越给定的距离&#12290;	@保罗.格雷汉姆 (知名黑客 硅谷牛人)",
			"在不同的层次审视你的设计",
			"在软件'可重用'之前先得'可用'	@Ralph Johnson (设计模式四人帮之一)",
			"软件设计就像做爱, 一次犯错, 你要用余下的一生来维护&#12290;	@Michael Sinz",
			"更好的工具未必能做出更好的设计",

			"好的程序员是那种过单行道马路都要往两边看的人	@Doug Linder",
			"好的程序代码本身就是最好的文档	@&#12298;代码大全&#12299;Steve McConnell",
			"假如程序代码和注释不一致, 那么很可能两者都是错的!	@Norm Schryer",
			"你写下的任何代码, 在六个月以后去看的话, 都像是别人写的&#12290;	@Tom Cargill",
			"程序必须首先让人类可以理解, 然后顺便让机器能执行&#12290;	@&#12298;SICP&#12299;",

			"不能影响你编程观点的语言, 不值得你去学&#12290;	@Alan Perlis",
			"世界上只有两种编程语言: 要么充满了抱怨; 要么没人使用&#12290;	@Bjarne Stroustrup (C++之父)",
			"没有哪种编程语言能阻止程序员写出糟糕的代码, 不管这种语言的结构有多么好&#12290;	@Larry Flon",
			"C语言诡异离奇, 缺陷重重, 但却获得了巨大的成功 &#65306;)	@Dennis Ritchie (C语言之父 Unix之父)",
			"(相对C而言)在C++里, 想搬起石头砸自己的脚更为困难了&#12290;	不过一旦你真这么做了, 整条腿都得报销!	@Bjarne Stroustrup (C++之父)",
			"Java与JavaScript的关系, 如同雷锋与雷峰塔的关系",

			"在理论上, 理论和实践是没有差异的; 但在实践中, 是有的&#12290;	In theory, there is no difference between theory and practice. But in practice, there is.	@Snepscheut",
			"在进度落后的项目中增加人手只会导致进度更加落后	@Fred Brooks (图灵奖得主 &#12298;人月神话&#12299;作者)  http://program-think.blogspot.com/2009/03/book-review-mythical-man-month.html",
			"用代码行数测算软件开发进度如同按重量测算飞机的制造进度	@比尔.盖茨",
			"在水上行走和按需求文档开发软件都很容易 -- 前提是它们都处于冻结状态	@Edward V Berard",
			"乐观主义是软件开发的职业病, 用户反馈则是其治疗方法&#12290;	@Kent Beck (Extreme Programming之父)",
			"软件开发是一场程序员和上帝的竞赛:	程序员要开发出更大更好&#12289;连傻瓜都会用的软件; 而上帝在努力创造更傻的傻瓜&#12290;	到目前为止, 一直是上帝赢&#12290;	@Rick Cook",

			"如果建筑工人像程序员写软件那样盖房子, 那第一只飞来的啄木鸟就能毁掉人类文明&#12290;	@Gerald Weinberg (软件工程大牛)",
			"如果调试(debug)是去除bug的过程, 那么编程就是制造bug的过程&#12290;	@Edsger Dijkstra (图灵奖得主)",
			"要在自己的代码里找出一个bug是十分困难的&#12290;而当你认为你的代码没有错误时, 那就更难了&#12290;	@&#12298;代码大全&#12299;Steve McConnel",
			"调试代码比新编写代码更困难&#12290;	因此, 如果你尽自己所能写出了最复杂的代码, 你将没有更大的智慧去调试它&#12290;",

			"过早的优化是万恶之源	Premature optimization is the root of all evil.	@Donald Knuth (算法大牛 图灵奖得主)",
			"Tape is Dead, Disk is Tape, Flash is Disk, RAM Locality is King!	@Jim Gray (数据库大牛 图灵奖得主)",

			"软件就像'性', 免费的时候更好!	Software is like sex; it's better when it's free.	@Linus Torvalds (Linux之父)"
	};
}
