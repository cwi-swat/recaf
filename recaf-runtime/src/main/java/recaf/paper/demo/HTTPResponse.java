package recaf.paper.demo;

public class HTTPResponse {
	private HTTPRequest req;
	private String js;

	public HTTPResponse(HTTPRequest req, String js) {
		this.req = req;
		this.js = js;
	}

	public String getJs() {
		return js;
	}
}
