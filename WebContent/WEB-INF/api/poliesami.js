"use strict";

window.PoliEsaMi = {};

window.PoliEsaMi.Model = class {
	constructor() {
		this._baseURL = /*[[${baseURL}]]*/ "";
		this._identity = null;
		this._headers = new Headers();
	}

	get identity() {
		return this._identity;
	}

	async login(user, password) {
		const headers = new Headers();
		const credential = btoa(user + ":" + password);
		headers.set("Authorization", "Basic " + credential);

		const response = await this._request("/login", {
			"headers" : headers
		});
		const text = await response.text();
		const obj = JSON.parse(text);
		if(obj.error == null) {
			this._identity = obj.data;
			this._headers.set("Authorization", "Bearer " + this.identity.jwt);
		}
		return obj;
	}

	logout() {
		this._identity = null;
		this._headers.delete("Authorization");
	}

	async test() {
		return this._get("/test");
	}

	async testInside() {
		return this._get("/inside/test");
	}

	async _get(path) {
		const init = {
			headers: this._headers
		};
		const response = await this._request(path, init);
		return response.json();
	}

	async _post(path, body) {
		const init = {
			method: "POST",
			headers: this._headers,
			body: body
		};
		const response = await this._request(path, init);
		return response.json();
	}

	async _request(path, init) {
		return fetch(this._baseURL + path, init);
	}
}
