package securecoding.controller.challenges.general;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.caoccao.javet.values.primitive.V8ValueInteger;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.mimosa.node.compiler.NodeCompiler;

@ChallengeController("/challenges/node-compiler-basics")
public class NodeCompilerBasicsChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = request.getParameter("code");
		attempt.setSubmission(code);

		try (NodeCompiler compiler = new NodeCompiler(attempt.getUser().getUsername())) {
			compiler.load("nhwr.js", code);
			compiler.load("grader.js", getTest());
			
			V8ValueInteger points = compiler.execute("grader.js");
			attempt.setPoints(points.toPrimitive());
		}
	}
	
	private String getTest() {
		StringBuilder script = new StringBuilder();
		script.append("const httpMocks = require(`node-mocks-http`);");
		script.append("let req = httpMocks.createRequest({ method: 'GET', url: '/' });");
		script.append("let res = httpMocks.createResponse();");

		script.append("const app = require(`./nhwr`);");
		script.append("app.handle(req, res);");
		script.append("let data = res._getData();");
		
		script.append("let score = 0;");
		script.append("if (res.statusCode == 200) score += 5;");
		script.append("if (data.message === 'helloworld') score += 10;");
		script.append("module.exports = score;");
		return script.toString();
	}

}
