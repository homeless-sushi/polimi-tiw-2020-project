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
		const credential = btoa(user + ":" + password);
		this._headers.set("Authorization", "Basic " + credential);

		const obj = await this._request("/login");
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
		return this._request("/test");
	}

	async testInside() {
		return this._request("/inside/test");
	}

	async getStudCoursesExams(studentId, year){
		return this._request(`/inside/student/exams?careerId=${studentId}` + (year != null ? `&year=${year}` : ""));
	}

	async getStudExam(studentId, examId){
		return this._request(`/inside/student/exams/exam?careerId=${studentId}&examId=${examId}`);
	}

	async getStudExamReg(studentId, examId){
		return this._request(`/inside/student/exams/exam/reg?careerId=${studentId}&examId=${examId}`);
	}

	async registerStudToExamReg(studentId, examId){
		const params = new URLSearchParams();
		params.set("action", "register");
		return this._request(`/inside/student/exams/exam/reg?careerId=${studentId}&examId=${examId}`, _POST(params));
	}

	async deregisterStudFromExamReg(studentId, examId){
		const params = new URLSearchParams();
		params.set("action", "deregister");
		return this._request(`/inside/student/exams/exam/reg?careerId=${studentId}&examId=${examId}`, _POST(params));
	}

	async rejectStudExamReg(studentId, examId){
		const params = new URLSearchParams();
		params.set("action", "reject");
		return this._request(`/inside/student/exams/exam/reg?careerId=${studentId}&examId=${examId}`, _POST(params));
	}

	async getProfCoursesExams(professorId, year){
		return this._request(`/inside/professor/exams?careerId=${professorId}` + (year != null ? `&year=${year}` : ""));
	}

	async getProfExam(professorId, examId){
		return this._request(`/inside/professor/exams/exam?careerId=${professorId}&examId=${examId}`);
	}

	async getProfExamRegistrations(professorId, examId){
		return this._request(`/inside/professor/exams/exam/regs?careerId=${professorId}&examId=${examId}`);
	}

	async getProfExamRegistration(professorId, examId, studentId){
		return this._request(`/inside/professor/exams/exam/regs/reg?careerId=${professorId}&examId=${examId}&studentId=${studentId}`);
	}

	async getProfExamRecords(professorId, examId){
		return this._request(`/inside/professor/exams/exam/records?careerId=${professorId}&examId=${examId}`);
	}

	async publishProfExamRegistrations(professorId, examId){
		const params = new URLSearchParams();
		params.set("action", "publish");
		return this._request(`/inside/professor/exams/exam/regs?careerId=${professorId}&examId=${examId}`, _POST(params));
	}

	async verbalizeProfExamRegistrations(professorId, examId){
		const params = new URLSearchParams();
		params.set("action", "verbalize");
		return this._request(`/inside/professor/exams/exam/regs?careerId=${professorId}&examId=${examId}`, _POST(params));
	}

	async editProfExamRegistrations(professorId, examId, examEvaluations){
		const params = new URLSearchParams();
		for(const examEvaluation of examEvaluations)
			params.append("evaluations", examEvaluation.join(","));
		return this._request(`/inside/professor/exams/exam/regs/edit?careerId=${professorId}&examId=${examId}`, _POST(params));
	}

	async _request(path, init = {}) {
		init.headers = this._headers;

		const response = await fetch(this._baseURL + path, init);
		if(response.status == 401)
			this.logout();
		try {
			return response.json();
		} catch(e) {
			return {error: {
				message: "The server returned an invalid response",
				cause: e.message
			}};
		}
	}
}

function _POST(body) {
	return {
		method: "POST",
		body: body
	};
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
