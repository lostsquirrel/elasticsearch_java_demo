package test.lisong.elastic.client;

import java.util.Date;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

public class TestBulkProcessor extends BaseTest {

	@Test
	public void test() throws Exception {
		BulkProcessor bulkProcessor = BulkProcessor.builder(
		        client,  
		        new BulkProcessor.Listener() {
		            @Override
		            public void beforeBulk(long executionId,
		                                   BulkRequest request) {
		            	System.out.println(executionId + " actions:" + request.numberOfActions());
		            } 

		            @Override
		            public void afterBulk(long executionId,
		                                  BulkRequest request,
		                                  BulkResponse response) { 
		            	
		            } 

		            @Override
		            public void afterBulk(long executionId,
		                                  BulkRequest request,
		                                  Throwable failure) {
		            	
		            } 
		        })
		        .setBulkActions(10) 
		        .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.KB)) 
		        .setFlushInterval(TimeValue.timeValueSeconds(5)) 
		        .setConcurrentRequests(1) 
		        .setBackoffPolicy(
		            BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)) 
		        .build();
		
		for (int i = 0; i < data.length; i++) {
			bulkProcessor.add(new IndexRequest("twitter", "tweet", ""+(2000 + i)).source(XContentFactory.jsonBuilder()
                    .startObject()
                    .field("user", data[i].split("@").length >= 2 ? data[i].split("@")[1] : "无名氏")
                    .field("postDate", new Date())
                    .field("message", data[i])
                .endObject()));
		}
	}
	
	private String[] data = {
			 "兴趣是最好的老师	@爱因斯坦  http://program-think.blogspot.com/2015/12/Hobbies-and-Interests.html",
		      "知识上的投资总能得到最好的回报&#12290;	@本杰明.富兰克林 (美国开国元勋 物理学家 作家)  http://program-think.blogspot.com/2013/09/knowledge-structure.html",
		      "学习不是填满水桶, 而是点燃火焰!	Education is not the filling of a pail but the lighting of a fire.	@叶芝 (爱尔兰诗人)",
		      "我唯一能确定的就是自己的无知	I know nothing except the fact of my ignorance.	@苏格拉底 (哲学之父)",
		      "真正的无知不是知识的贫乏, 而是拒绝获取知识!	@波普尔 (哲学家 思想家)",
		      "读书是在别人思想的帮助下建立自己的思想	@尼古拉.鲁巴金  (俄国作家)  http://program-think.blogspot.com/2013/04/how-to-read-book.html",
		      "不要盲目地崇拜任何权威, 因为你总能找到相反的权威&#12290;	@罗素 (哲学家 数学家)  http://program-think.blogspot.com/2014/05/fans-and-idolatry.html",
		      "不必为自己的独特看法而害怕, 因为我们现在所接受的常识都曾是独特看法&#12290;	@&#12298;自由思想的十诫&#12299;罗素 (哲学家 数学家)",
		      "仅仅凭借信仰跟从就等于盲从	To follow by faith alone is to follow blindly.	@本杰明.富兰克林 (美国开国元勋 物理学家 作家)",
		      "想象力比知识更重要!	因为知识是有限的, 而想象力概括着世界的一切, 推动着进步, 并且是知识进化的源泉&#12290;	@爱因斯坦",
		      "要打破人的偏见比崩解一个原子还难&#12290;	@爱因斯坦",
		      "你要按你所想的去生活; 否则, 你迟早会按你所生活的去想!",
		      "大多数人宁愿死去, 也不愿思考&#12290; -- 事实上他们也确实到死都没有思考&#12290;	@罗素 (哲学家 数学家)",
		      "对知识分子而言, 成为思维的精英比成为道德的精英更重要&#12290;	@王小波 (作家)",
		      "只有两样东西可能是无限的: 宇宙的大小和人类的愚蠢&#12290;不过, 对于前者我不太确定 :)	@爱因斯坦",
		      "一个人的价值, 在于他贡献了什么, 而不在于他获得了什么&#12290;	@爱因斯坦",
		      "我从来不把安逸和快乐看作是生活目的本身, 对这种伦理基础, 我称之为'猪栏的理想'&#12290;	@爱因斯坦",
		      "未经反思自省的人生不值得去过	The unexamined life is not worth living.	@苏格拉底 (哲学之父)",
		      "大多数人在20到30岁就已经过完自己的一生&#12290;	一过了这个年龄段, 他们就变成自己的影子, 以后的生命只是在不断重复自己&#12290;	@&#12298;约翰.克里斯朵夫&#12299;罗曼.罗兰",
		      "活着, 如同生命最后一天般活着;	学习, 如同永远活着般学习&#12290;	@圣雄甘地 (印度国父)",
		      "人所处的绝境, 在很多情况下, 都不是生存的绝境, 而是精神的绝境!",
		      "对爱情的渴望, 对知识的追求, 对人类苦难不可遏制的同情心, 这三种纯洁而无比强烈的激情支配着我的一生&#12290;	Three passions, simple but overwhelmingly strong, have governed my life: the longing for love, the search for knowledge, and unbearable pity for the suffering of mankind.	@&#12298;我为什么而活着&#12299;罗素 (哲学家 数学家)",
		      "人生中最大的两个财富是: 你的才华和你的时间&#12290;	才华越来越多而时间越来越少&#12290;我们的一生就是用时间来换取才华&#12290;",
		      "Stay hungry. Stay foolish.	@史蒂夫.乔布斯引自&#12298;全球概览&#12299;",
		      "这辈子没法做太多的事情, 所以每一件都要做到精彩绝伦!	@史蒂夫.乔布斯",
		      "你们的时间有限, 所以不要浪费时间活在别人的生活里&#12290;	Your time is limited, so don't waste it living someone else's life.	@史蒂夫.乔布斯",
		      "拥有追随自己内心与直觉的勇气, 你的内心与直觉多少已经知道你真正想要成为什么样的人&#12290;	Have the courage to follow your heart and intuition. They somehow already know what you truly want to become.	@史蒂夫.乔布斯",
		      "我每天都自问: '如果今天是我生命的最后一天, 我还会做今天打算做的事情吗?'	@史蒂夫.乔布斯  http://program-think.blogspot.com/2009/01/4.html",
		      "想得到你从未拥有过的东西, 就必须做你从未做过的事情&#12290;",
		      "预测未来最好的方法就是去创造未来	@林肯 (美国前总统)",
		      "没有人可以回到过去重新开始, 但谁都可以从现在开始, 书写一个全然不同的结局!",
		      "人生最大的一种痛, 不是失败, 而是没有经历自己想要经历的一切&#12290;",
		      "你若不想做, 总能找到借口; 你若想做, 总会找到方法	@阿拉伯谚语",
		      "宁鸣而死 不默而生	@范仲淹",
		      "获得信任的技巧就是避免使用任何技巧",
		      "判断一个人的人品, 不是看他好起来做什么好事, 而是看他坏起来&#12304;不做&#12305;什么坏事&#12290;",
		      "不要去欺骗别人 -- 因为你能骗到的人, 都是相信你的人&#12290;",
		      "想要成功, 就要学会在机遇从头顶上飞过时跳起来并抓住它&#12290;	这样逮到机遇的概率才大!	@比尔.盖茨",
		      "授人以鱼不如授人以渔!	授人以鱼只救一时之急, 授人以渔则可解一生之需&#12290;	(这也是俺博客的宗旨)",
		      "在民主国家, 最高原则是全民的利益而不是统治者的利益&#12290;	服从民主国家的统治权并不会使人变为奴隶, 而是使人变为公民&#12290;	@斯宾诺莎 (哲学家)",
		      "国家为人而立, 而非人为国家而活&#12290;	国家的最高使命是保护个人, 使其有机会发展成为有创造才能的人&#12290;	@爱因斯坦",
		      "如果政府不能解决问题, 那它本身就成为问题!	@里根 (美国前总统)",
		      "民众不应该害怕他们的政府, 政府才应该害怕它的民众!	People should not be afraid of their governments. Governments should be afraid of their people.	@&#12298;V怪客&#65295;V字仇杀队&#12299;  http://program-think.blogspot.com/2011/11/film-v-for-vendetta.html",
		      "制造恐惧是专制政府的终极武器	@&#12298;V怪客&#65295;V字仇杀队&#12299;  http://program-think.blogspot.com/2011/11/film-v-for-vendetta.html",
		      "宪法的基本原则是: 个人可以做任何事情, 除非法律禁止; 政府不能做任何事情, 除非法律许可&#12290;	@约翰.洛克 (哲学家 思想家)",
		      "财产不应公有, 权力不应私有&#12290;否则将会坠入地狱&#12290;	@约翰.洛克 (哲学家 思想家)",
		      "爱国者的责任就是保护国家不受政府侵犯	@托马斯&#183;潘恩 (美国政治思想家)",
		      "没有投票权的征税就是暴政	@詹姆斯.奥蒂斯 (美国独立时期评论家)",
		      "当法律失去公正, 则反抗成为义务&#12290;	When injustice becomes law, resistance becomes duty.",

		      "牺牲基本自由来换取暂时的安全, 最后既得不到安全也得不到自由!	@本杰明.富兰克林 (美国开国元勋 物理学家 作家)",
		      "现在有人对你们说: '牺牲你们个人的自由, 去求国家的自由!'	我要对你们说: '争取个人的自由, 就是争取国家的自由; 争取个人的人格, 就是争取国家的国格&#12290;自由平等的国家不是一群奴才建造得起来的!'	@胡适 (思想家)  http://program-think.blogspot.com/2013/11/weekly-share-57.html",
		      "美国人来了, 有面包有自由; 苏俄来了, 有面包无自由; 中共来了, 无面包无自由!	@胡适 (思想家)  http://program-think.blogspot.com/2014/07/artists-and-ccp.html",
		      "若批评不自由, 则赞美无意义!	@法国&#12298;费加罗报&#12299;的宗旨",
		      "法律本身并不能保证言论自由; 要做到这一点, 必须所有人都有包容精神&#12290;	Laws alone can not secure freedom of expression;	in order that every man present his views without penalty there must be spirit of tolerance in the entire population.	@爱因斯坦",
		      "解放一个习惯于被奴役的民族比奴役一个习惯于自由的民族更难	@孟德斯鸠 (启蒙思想家)",
		      "将愚人从他们所敬拜的锁链下解放出来是非常困难的	@伏尔泰 (启蒙思想家)",

		      "专政与民主是对立的统一, 人民民主是基础, 只有充分民主才能有专政, 离开民主就是法西斯专政!	@胡耀邦谈'人民民主专政'",
		      "如果人民不欢迎我们, 我们就该下台了!	@胡耀邦",
		      "民主是自下而上争取的, 不是自上而下给与的!	@方励之 (科学家 政治异议人士)",
		      "民主并非只是选举投票&#65292;它是生活方式&#65292;是思维方式&#65292;是你每天呼吸的空气&#12289;举手投足的修养&#65292;个人回转的空间&#12290;	@龙应台 (台湾作家)",

		      "高等教育的价值在于培训思维, 而不在于传授事实&#12290;	The value of a college education is not the learning of many facts but the training of the mind to think.	@爱因斯坦",
		      "父亲们最根本的缺点, 在于想要自己的孩子为自己争光&#12290;	The fundamental defect of fathers is that they want their children to be a credit to them.	@罗素 (哲学家 数学家)",
		      "花在孩子身上的钱和孩子的修养之间没有任何关系, 甚至成反比&#12290;	在子女教育方面, 父母应该投入的是时间, 而不是金钱&#12290;	@大前研一 (日本经济评论家)",
		      "小时候一个劲地教你做好人, 长大了一个劲地教你做坏人	这就是中国式教育",

		      "你可以暂时地蒙骗所有人, 也可以永久地蒙骗部分人, 但不可能永久地蒙骗所有人&#12290;	You can fool all the people some of the time, some of the people all the time, but you cannot fool all the people all the time.	@林肯 (美国前总统)",
		      "党可以宣布'2+2=5', 而你就不得不相信它&#12290;	@&#12298;1984&#12299;乔治.奥威尔  http://program-think.blogspot.com/2009/06/book-review-1984.html",
		      "在大欺骗的时代, 说出真相就是革命行为!	@乔治.奥威尔 (&#12298;1984&#12299;作者)",
		      "艺术家用谎言揭露真相, 政治家用谎言掩盖真相&#12290;	Artists use lies to tell the truth, while politicians use them to cover the truth up.	@&#12298;V怪客&#65295;V字仇杀队&#12299;  http://program-think.blogspot.com/2011/11/film-v-for-vendetta.html",
		      "任何专制国家的教育, 其目的都是在极力降低国民的心智&#12290;	@孟德斯鸠 (启蒙思想家)",
		      "全中国只有一所学校, 就是党校 -- 其它的学校都是分校!	@陈丹青 (艺术家)",
		      "古代愚民政策是不许民众受教育, 现代愚民政策是只许民众受洗脑教育",
		      "洗脑教育要塑造的, 不是铁屋中沉睡的人, 而是装睡的人&#12290;	因为沉睡的人你总有办法唤醒, 但是你永远无法唤醒装睡的人!",
		      "当你试图了解你的祖国, 你已经走上了犯罪道路!	@艾未未 (艺术家 持不同政见者)",

		      "世上最难的两件事: 把自己的思想装进别人的脑袋, 把别人的钞票装进自己的口袋 -- 共产党都做到了!",
		      "孔丘&#12289;朱熹的奴隶少了, 却添上了一班马克思&#12289;克鲁泡特金的奴隶&#12290;	@胡适 (思想家)",
		      "革命以前, 做奴隶; 革命后不久, 受了奴隶的骗, 变成他们的奴隶!	@鲁迅",
		      "以前学英语是为了更好地了解外国; 现在学英语是为了更好地了解中国!",

		      "共产主义是一种伪科学, 演变成一种伪宗教, 最终表现为僵化的集权式的邪恶政治集团!	@Richard Pipes (&#12298;共产主义实录&#12299;作者)",
		      "作为一名预言家, 马克思失败的原因, 完全在于历史主义的贫乏&#12290;	@&#12298;历史决定论的贫困&#12299;波普尔 (哲学家 思想家)",
		      "总是使一个国家变成人间地狱的东西, 恰恰是人们试图将其变成天堂&#12290;	@荷尔德林 (哈耶克的&#12298;通往奴役之路&#12299;第2章把此句作为引言)",
		      "如何判断什么样的人是共产主义者呢, 共产主义者就是那些阅读马克思和列宁学说的人;	那如何判断反共产主义者呢&#65292;反共产主义者是那些真正理解了马克思和列宁学说的人&#12290;	@里根 (美国前总统)",
		      "一个人如果30岁以前没有相信过共产主义, 他缺乏良心; 如果到30岁以后还相信共产主义, 他没有头脑&#12290;	@克列蒙梭 (法国政治家)",
		      "共产主义最大的优越性体现在: 可以克服别的主义下根本不存在的困难&#12290;",

		      "一百年了都没长进 -- 上面还是慈禧, 下面还是义和团!	@资中筠 (中国社科院学者)",
		      "如果让中宣部的官员和卫生部的官员对调, 那么中国既有了言论自由, 也有了食品安全&#12290;",
		      "中共是这样的政党 -- 既千方百计阻止你知道真相, 又千方百计指责你不明真相 :)",
		      "中国共产党是一心一意为人民服务的政党 -- 你想不让它服务都不行 :)",
		      "'中国模式'的核心竞争力就是压榨劳动力	@谢国忠 (经济学家)",
		      "不是具有中国特色的社会主义, 而是具有中国特色的社会达尔文主义!",
		      "欧美的精英们已经不再为生存而担忧, 不用因恐惧而说话&#12290;而中国的精英们还在为民主自由而耗尽精力甚至生命!",
		      "如果鲁迅活在这个年代: 他的博客首先会被和谐, 然后被请喝茶谈话, 最后以煽动颠覆国家罪被捕入狱...",
		      "一个国家的监狱里有一个良心犯, 这个国家就不会有良心; 有二个, 这个国家就让人恶心; 有三个, 这就不是国家; 有四个, 亡国就是解放&#12290;	@昂山素季 (缅甸民运领袖)",
		      "道德在书本里, 榜样在电视里, 国土在肺里, 爱情在房产证里, 幸福感在梦里...	这就是中国特色",
		      "拜金不可怕, 可怕的是在一个不吃不喝也要几百年才能买房的社会却不许拜金;	低俗不可怕, 可怕的是在一个几千万男生找不到女友, 同龄少女都被老男人包养的国度却不准低俗!",
		      "郭敬明和唐骏的共同点是: 他们这类人越成功, 就说明我们这个社会越失败!",
		      "中国没有多少'人民内部矛盾', 主要是'党和人民的矛盾'&#12290;党反复提'人民内部矛盾', 其实是挑拨离间!",
		      "天朝知识分子分三类: 1 沉默的大多数 2 公共知识分子 3 '公公'知识分子",

		      "权力导致腐败, 绝对的权力导致绝对的腐败!	@阿克顿勋爵 (政治思想家)",
		      "一切拥有权力的人都有滥用权力为自己谋求私利的倾向	@孟德斯鸠 (启蒙思想家)  http://program-think.blogspot.com/2014/07/corruption-and-form-of-government.html",
		      "一群亿万富豪在人民大会堂里开两会 -- 他们管自己叫'无产阶级先锋队'  http://program-think.blogspot.com/2012/03/national-people-congress.html",
		      "中国人民是伟大的	用全球7%的耕地养活了全球50%的公务员, 并承受全球70%的官员腐败...",

		      "罗马城之所以是这样的罗马城是因为市民就是这样的市民!	This City is what it is because our citizens are what they are.	@柏拉图",
		      "一个肮脏的国家, 如果人人讲规则而不是空谈道德, 最终会变成一个有人味儿的正常国家, 道德自然会逐渐回归;	反之, 一个干净的国家, 如果人人都不讲规则却大谈道德&#12289;谈高尚, 天天没事儿就谈道德规范, 人人大公无私, 最终这个国家会堕落成为一个伪君子遍布的肮脏国家&#12290;	@胡适 (思想家)  http://program-think.blogspot.com/2013/11/weekly-share-57.html",
		      "你要看一个国家的文明, 只需考察三件事: 第一看他们怎样待小孩子; 第二看他们怎样待女人; 第三看他们怎样利用闲暇的时间&#12290;	@胡适 (思想家)",
		      "做奴隶虽然不幸, 但并不可怕, 因为知道挣扎, 毕竟还有挣脱的希望;	若是从奴隶生活中寻出美来, 赞叹, 陶醉, 就是万劫不复的奴才了!	@鲁迅  http://program-think.blogspot.com/2012/06/stockholm-syndrome.html",
		      "自有历史以来, 中国人是一向被同族屠戮&#12289;奴隶&#12289;敲掠&#12289;刑辱&#12289;压迫下来的, 非人类所能忍受的楚痛, 也都身受过&#12290;	每一考查, 真教人觉得不像活在人间&#12290;	@鲁迅",
		      "信仰不能当饭吃, 所以不重要; 民主不能当饭吃, 所以不重要; 自由不能当饭吃, 所以不重要......	对于中国人来说, 不能当饭吃的, 都不重要&#12290;	我们信奉了猪的生活原则, 也就得到了猪的命运 -- 迟早给别人当饭吃",

		      "真的猛士敢于在一个不正常的国家做一个正常的人",
		      "一旦你习惯了戴面具的生活, 你的脸将变得跟面具一样&#12290;	@&#12298;V怪客&#65295;V字仇杀队&#12299;  http://program-think.blogspot.com/2010/11/institutionalize.html",
		      "每当有事情发生, 懦夫会问: '这么做安全吗?' 患得患失者会问: '这么做明智吗?' 虚荣者会问: '这么做受欢迎吗?' 但是良知只会问: '这么做正确吗?'	@马丁.路德.金 (美国人权领袖)",
		      "雪崩时, 没有一片雪花觉得自己有责任&#12290;	@伏尔泰 (启蒙思想家)",
		      "千万别以为自己可以逃避, 我们的每一步都决定着最后的结局, 我们的脚正在走向自己选定的终点&#12290;	@米兰.昆德拉 (作家)",
		      "人道主义的含义是: 从不以人作为牺牲来达到某一目的!	@施韦策 (诺贝尔和平奖得主)",
		      "所谓'摸着石头过河'就是: 群众们都过河了, 官员们还在那里假装摸石头!",
		      "谁控制过去, 谁就控制未来; 谁控制现在, 谁就控制过去&#12290;	Who controls the past controls the future; who controls the present controls the past.	@&#12298;1984&#12299;乔治.奥威尔  http://program-think.blogspot.com/2009/06/book-review-1984.html",
		      "以铜为镜, 可以正衣冠; 以史为镜, 可以知兴替; 以人为镜, 可以明得失&#12290;	@李世民 (唐太宗)",
		      "人类从历史学到的唯一教训就是: 人类没有从历史中学到任何教训&#12290;	@汤因比 (历史学家)",
		      "人们总以为自己生活的时代糟糕透顶, 总是向往过去的黄金时代&#12290;	但在我们如今认为是身处黄金年代的那些人看来, 他们当时所处的世界同样是苍白无力的&#12290;	@伍迪.艾伦",
		      "中国人最悲哀的就是: 刚被历史的车轮碾过, 还没来得及爬起来, 发现历史在倒车...",
		      "自由有许多困难, 民主亦非完美&#12290;然而, 我们从未建造一堵墙, 把我们的人民关在里面, 不准他们离开&#12290;	@&#12298;在柏林墙下的演说&#12299;肯尼迪 (美国前总统)  http://program-think.blogspot.com/2009/07/break-through-berlin-wall.html",
		      "这些(监狱的)围墙很有趣&#12290;起初你痛恨它; 然后你逐渐习惯它; 足够长时间后, 你开始依赖它 -- 这就是体制化!	@电影&#12298;肖申克的救赎&#12299;  http://program-think.blogspot.com/2010/11/institutionalize.html",
		      "Google重新发明了搜索, Facebook重新发明了社交, Apple重新发明了手机, Amazon重新发明了书籍...	天朝重新发明了局域网",
		      "翻墙和OX的相似之处:	一旦会做就老想做; 做第一次之后觉得天地豁然开朗; 每次做都有快感; 觉得不会做的都是SB!",
		      "GFW把中国人挡在了无数优秀网站之外, 仿佛在这些网站入口处设置了一道铁门, 上书八个大字:'华人与狗 不得入内'",
		      "几十年来, 朝鲜的领导人只有一个, 叫'金正日'; 几十年来, 天朝的领导人也只有一个, 叫'敏感词'",
		      "宁要社会主义的防火墙 不要资本主义的互联网",
		      "用人不在于如何减少人的短处, 而在于如何发挥人的长处&#12290;	@彼得.德鲁克 (管理学之父)",
		      "企业最大的资产是人!	@松下幸之助 (号称日本经营之神)",
		      "你想雇用的人必须具备3种品质: 正直诚实&#12289;聪明能干和精力充沛&#12290;如果缺少第一种, 后两种品质会要你命!	@巴菲特  http://program-think.blogspot.com/2009/04/defect-of-hire.html",
		      "以用户为中心, 其它一切纷至沓来!	@Google 信条",
		      "只有偏执狂才能生存!	@Andy Grove (英特尔创始人之一, 前任CEO)",
		      "领袖和跟风者的区别就在于创新!	@史蒂夫.乔布斯",
		      "我的管理风格既不是美国的个人主义, 也不是日本的共识主义, 而是独特的达尔文主义(适者生存)!	@比尔.盖茨",
		      "我们没有不懂技术的管理人员 -- 因为寻求技术和管理的平衡毫不费力!	@比尔.盖茨",
		      "伟大的车工值得给他几倍于普通车工的薪水&#12290;但一个伟大的程序员, 其价值相当于普通程序员的1万倍!	@比尔.盖茨",
		      "当你用一个手指指着某人时, 请注意其它三个手指在指哪儿&#12290;	@Gerald Weinberg (软件工程大牛)  http://program-think.blogspot.com/2009/07/book-review-are-your-lights-on.html",
		      "唯一不变的是变化本身!",
		      "我也会有恐惧和贪婪, 只不过是在大众贪婪时恐惧, 在大众恐惧时贪婪!	@巴菲特",
		      "控制风险的最好办法是深入思考, 而不是投资组合&#12290;	@巴菲特",
		      "价值投资不能保证我们盈利, 但价值投资给我们提供了通向成功的唯一机会&#12290;	@巴菲特",
		      "我从事投资时会观察一家公司的全貌; 而大多数投资人只盯着它的股价&#12290;	@巴菲特",
		      "投资成功与否并非取决于你了解的东西, 而在于你能否老老实实地承认你所不知道的东西&#12290;	投资人并不需要做对很多事情, 重要的是不要犯重大的错误&#12290;	@巴菲特",
		      "退潮时便可知道谁在裸泳&#12290;	@巴菲特",
		      "短期而言, 股票市场是个投票机; 长期而言, 股票市场是个称重器&#12290;	@本杰明.格雷厄姆",
		      "中国股市比赌场还不如 -- 因为在中国股市&#65292;某些人可以看别人的底牌&#12290;	@吴敬琏",
		      "投资是预测资产收益的活动, 而投机是预测市场心理的活动&#12290;	@凯恩斯"
	};
}
