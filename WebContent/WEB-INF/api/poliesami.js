"use strict";

window.PoliEsaMi = {};

window.PoliEsaMi.Model = class {
	constructor() {
		this._baseURL = /*[[${baseURL}]]*/ "";
	}

	async test() {
		return this._get("/test");
	}

	async _get(path) {
		const init = {
		};
		const response = await this._request(path, init);
		return response.json();
	}

	async _post(path, body) {
		const init = {
			method: "POST",
			body: body
		};
		const response = await this._request(path, init);
		return response.json();
	}

	async _request(path, init) {
		return fetch(this._baseURL + path, init);
	}
}
