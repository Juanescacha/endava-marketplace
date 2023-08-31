import axios from "axios";

const makePostRequest = async (route, data) => {
	try {
		const response = await axios.post(route, data);

		const { status } = response;
		const msg =
			status === 201 || status === 200
				? "Succesfully operation"
				: "Failed operation";
		return { response, msg };

	} catch (error) {
		const {
			response: { status },
		} = error;

		const msg =
			status === 500
				? "An internal error in the server has ocurred"
				: "Failed connection with the server";

		return { err: msg };
	}
};

export { makePostRequest };
