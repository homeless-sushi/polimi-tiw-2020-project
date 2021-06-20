"use strict";

window.PoliEsaMi = {};

window.PoliEsaMi.Model = class {
	constructor() {
		this._baseURL = /*[[${baseURL}]]*/ "";
		this._headers = new Headers();

		this.identity = PoliEsaMi.storage.getItemJSON("identity");
	}

	get identity() {
		return this._identity;
	}

	set identity(identity) {
		if(identity == null) {
			this._headers.delete("Authorization");
		} else {
			this._headers.set("Authorization", "Bearer " + identity.jwt);
		}
		this._identity = identity;
	}

	async login(user, password, keep = false) {
		const headers = new Headers();
		const credential = btoa(user + ":" + password);
		headers.set("Authorization", "Basic " + credential);

		const response = await this._request("/login", {
			"headers" : headers
		});
		const obj = await response.json();
		if(obj.error == null) {
			this.identity = obj.data;
			PoliEsaMi.storage.setItemJSON("identity", obj.data, keep);
		}
		return obj;
	}

	logout() {
		this.identity = null;
		PoliEsaMi.storage.removeItem("identity");
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

window.PoliEsaMi.storage = {
	setItem(name, value, permanent) {
		try {
			const storage = permanent ? localStorage : sessionStorage;
			storage.setItem(name, value);
			return true;
		} catch(e) {
			console.warn(e.message);
		}
		return false;
	},

	setItemJSON(name, value, permanent) {
		try {
			const json = JSON.stringify(value);
			return this.setItem(name, json, permanent);
		} catch(e) {
			console.warn(e.message);
		}
		return false;
	},

	getItem(name) {
		try {
			let obj;
			obj = sessionStorage.getItem(name);
			if(obj != null)
				return obj;
			obj = localStorage.getItem(name);
			if(obj != null)
				return obj;
		} catch(e) {
			console.warn(e.message);
		}
		return null;
	},

	getItemJSON(name) {
		try {
			return JSON.parse(this.getItem(name));
		} catch(e) {
			console.warn(e);
		}
		return null;
	},

	removeItem(name) {
		try  {
			sessionStorage.removeItem(name);
			localStorage.removeItem(name);
		} catch(e) {
			console.warn(e.message);
		}
	}
}
