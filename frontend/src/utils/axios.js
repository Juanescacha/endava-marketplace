import axios from "axios";
import { getCookie } from "./cookies";
import { addParamsToURL } from "./strings";

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

export const postNewListing = async data => {
	try {
		let url = getAPIURL();
		url += "/api/listings";

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

export const postImagesOfListing = async (listingId, images = []) => {
	try {
		const url = `${getAPIURL()}/api/listings/images/${listingId}`;

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

export const postSale = async data => {
	try {
		const url = `${getAPIURL()}/api/sales`;
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

export const patchSaleStatus = async (saleId, operation) => {
	try {
		const url = `${getAPIURL()}/api/sales/${saleId}/${operation}`;
		const config = {
			headers: getHeadersForRequest(),
		};

		const { status } = await axios.patch(url, {}, config);

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

export const deleteListing = async id => {
	try {
		const url = `${getAPIURL()}/api/listings/${id}`;
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

export const postUser = async () => {
	try {
		const url = `${getAPIURL()}/api/endavans`;
		const config = {
			headers: getHeadersForRequest(),
		};
		const response = await axios.post(url, {}, config);
		return response.data;
	} catch (error) {
		return handleCatch(error);
	}
};

export const getSellerSales = async id => {
	try {
		const url = `${getAPIURL()}/api/sales/seller/${id}`;
		const config = {
			headers: getHeadersForRequest(),
		};
		const { status, data } = await axios.get(url, config);

		if (status !== 200) {
			return {
				msg: "An internal error in the server has ocurred",
				error: true,
			};
		}
		return { data };
	} catch (error) {
		return handleCatch(error);
	}
};

export const getUserPurchases = async id => {
	try {
		const url = `${getAPIURL()}/api/sales/buyer/${id}`;
		const config = {
			headers: getHeadersForRequest(),
		};
		const { status, data } = await axios.get(url, config);

		if (status !== 200) {
			return {
				msg: "An internal error in the server has ocurred",
				error: true,
			};
		}
		return { data };
	} catch (error) {
		return handleCatch(error);
	}
};

export const getListingById = async id => {
	const url = `${getAPIURL()}/api/listings/${id}`;
	return await makeGetRequest(url);
};

export const getListingImages = async id => {
	const url = `${getAPIURL()}/api/listings/images/${id}`;
	return await makeGetRequest(url);
};

export const getListingThumbanil = async id => {
	const url = `${getAPIURL()}/api/listings/thumb/${id}`;
	return await makeGetRequest(url);
};

export const getSaleById = async id => {
	const url = `${getAPIURL()}/api/sales/${id}`;
	return await makeGetRequest(url);
};

export const getListingsSearch = async (params = {}) => {
	let url = `${getAPIURL()}/api/listings/search`;
	url = addParamsToURL(url, params);
	return await makeGetRequest(url);
};

export const getListingsSuggestions = async name => {
	const url = `${getAPIURL()}/api/listings/suggestions?name=${name}`;
	return await makeGetRequest(url);
};

export const isLogedUserAdmin = async () => {
	const url = `${getAPIURL()}/api/endavans/isAdmin`;
	return await makeGetRequest(url);
};

export const patchAdminRole = async (id, isAdmin) => {
	const numId = Number(id);
	if (Number.isNaN(numId) || numId < 1 || typeof isAdmin !== "boolean") {
		return { error: true };
	}
	const url = `${getAPIURL()}/api/endavans/admin?endavanId=${id}&isAdmin=${isAdmin}`;
	const config = {
		headers: getHeadersForRequest(),
	};

	try {
		const { status } = await axios.patch(url, {}, config);
		if (status !== 200) {
			return {
				msg: "An internal error in the server has ocurred",
				error: true,
			};
		}
		return { msg: "Succesful operaton" };
	} catch (error) {
		handleCatch(error);
	}
};

export const getUsers = async (page = 1, filters = {}) => {
	let url = `${getAPIURL()}/api/endavans/search`;
	const auxFilters = { page, ...filters };
	url = addParamsToURL(url, auxFilters);
	return await makeGetRequest(url);
};

export const getListingsByCategory = async id => {
	const url = `${getAPIURL()}/api/listings/search/get?category=${id}`;
	return await makeGetRequest(url);
};

export const getProfileImage = async id => {
	const url = `${getAPIURL()}/api/endavans/picture`;
	try {
		const config = {
			headers: getHeadersForRequest(),
			responseType: "arraybuffer",
		};

		const response = await axios.get(url, config);
		return response;
	} catch (error) {
		return handleCatch(error);
	}
};

// CATEGORIES

export const getAllCategories = async () => {
	const url = `${getAPIURL()}/api/categories`;
	return await makeGetRequest(url);
};

export const getActiveCategories = async () => {
	const url = `${getAPIURL()}/api/categories/active`;
	return await makeGetRequest(url);
};

export const postCategory = async name => {
	const url = `${getAPIURL()}/api/categories?name=${name}`;
	const config = {
		headers: getHeadersForRequest(),
	};
	try {
		const response = await axios.post(url, {}, config);
		return response.data;
	} catch (error) {
		return handleCatch(error);
	}
};

export const patchCategoryName = async (id, name) => {
	const url = `${getAPIURL()}/api/categories/${id}/rename?name=${name}`;
	const config = {
		headers: getHeadersForRequest(),
	};
	try {
		const response = await axios.patch(url, {}, config);
		return response.data;
	} catch (error) {
		return handleCatch(error);
	}
};

export const patchEnableCategory = async id => {
	const url = `${getAPIURL()}/api/categories/${id}/enable`;
	const config = {
		headers: getHeadersForRequest(),
	};
	try {
		const response = await axios.patch(url, {}, config);
		return response.data;
	} catch (error) {
		return handleCatch(error);
	}
};

export const patchDisableCategory = async id => {
	const url = `${getAPIURL()}/api/categories/${id}/disable`;
	const config = {
		headers: getHeadersForRequest(),
	};
	try {
		const response = await axios.patch(url, {}, config);
		return response.data;
	} catch (error) {
		return handleCatch(error);
	}
};
