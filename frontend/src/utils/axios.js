import axios from "axios";
import { getCookie } from "./cookies";

const getAPIURL = () => import.meta.env.VITE_API_URL;

const getHeadersForRequest = () => ({
	Authorization: `Bearer ${getCookie("access_token")}`,
});

const handleCatch = error => {
	const status = error.response?.status;

	const msg =
		status === 500
			? "An internal error in the server has ocurred"
			: "Failed connection with the server";

	return { error, msg };
};

const makeGetRequest = async route => {
	try {
		const config = {
			headers: getHeadersForRequest(),
		};

		const response = await axios.get(route, config);
		return response;
	} catch (error) {
		return handleCatch(error);
	}
};

const postNewListing = async data => {
	try {
		let url = getAPIURL();
		url += "/api/listings/post";

		const config = {
			headers: getHeadersForRequest(),
		};

		const response = await axios.post(url, data, config);

		const { status } = response;
		const msg =
			status === 201 || status === 200
				? "Succesfull operation"
				: "Failed operation";
		return { response, msg };
	} catch (error) {
		return handleCatch(error);
	}
};

const postImagesOfListing = async (listingId, images = []) => {
	try {
		const url = `${getAPIURL()}/api/listings/post/images/${listingId}`;

		const form = new FormData();
		images.forEach(image => {
			form.append("images", image);
		});

		const config = {
			headers: {
				"Content-Type": "multipart/form-data",
				...getHeadersForRequest(),
			},
		};

		const response = await axios.post(url, form, config);

		if (!response || response.status !== 200) {
			return {
				msg: "An internal error in the server has ocurred",
				error: true,
			};
		}

		return {};
	} catch (error) {
		return handleCatch(error);
	}
};

const postSale = async data => {
	try {
		const url = `${getAPIURL()}/api/sales/post`;
		const config = {
			headers: getHeadersForRequest(),
		};

		const { status } = await axios.post(url, data, config);

		if (status !== 200) {
			return {
				msg: "An internal error in the server has ocurred",
				error: true,
			};
		}
		return {};
	} catch (error) {
		return handleCatch(error);
	}
};

const deleteListing = async id => {
	try {
		const url = `${getAPIURL()}/api/listings/delete/${id}`;
		const config = {
			headers: getHeadersForRequest(),
		};
		const { status } = await axios.delete(url, config);

		if (status !== 200) {
			return {
				msg: "An internal error in the server has ocurred",
				error: true,
			};
		}
		return {};
	} catch (error) {
		return handleCatch(error);
	}
};

const postUser = async () => {
	try {
		const url = `${getAPIURL()}/api/user/post`;
		const config = {
			headers: getHeadersForRequest(),
		};
		const response = await axios.post(url, {}, config);
		return response.data;
	} catch (error) {
		return handleCatch(error);
	}
};

export {
	makeGetRequest,
	postNewListing,
	postImagesOfListing,
	postSale,
	deleteListing,
	postUser,
};
