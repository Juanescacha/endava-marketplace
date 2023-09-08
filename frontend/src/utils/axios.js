import axios from "axios";
import { getCookie } from "./cookies";

const makeGetRequest = async route => {
	try {
		const config = {
			headers: {
				Authorization: `Bearer ${getCookie("access_token")}`,
			},
		};

		const response = await axios.get(route, config);
		return response;
	} catch (error) {
		const status = error.response?.status;

		const msg =
			status === 500
				? "An internal error in the server has ocurred"
				: "Failed connection with the server";

		return { error, msg };
	}
};

const makePostRequest = async (route, data) => {
	try {
		const config = {
			headers: {
				Authorization: `Bearer ${getCookie("access_token")}`,
			},
		};
		const response = await axios.post(route, data, config);

		const { status } = response;
		const msg =
			status === 201 || status === 200
				? "Succesfully operation"
				: "Failed operation";
		return { response, msg };
	} catch (error) {
		const status = error.response?.status;

		const msg =
			status === 500
				? "An internal error in the server has ocurred"
				: "Failed connection with the server";

		return { error, msg };
	}
};

export { makeGetRequest, makePostRequest };
