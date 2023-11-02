const capitalizeFirstLetter = word => {
	if (typeof word !== "string" || word.length < 2) {
		throw new Error("Not a valid word to capitalize");
	}
	let firstLetter = word.charAt(0);
	const remainingLetters = word.slice(1);

	firstLetter = firstLetter.toUpperCase();
	return firstLetter + remainingLetters;
};

const getArticleOfSentence = (numberOfElements, firstLetterOfNextWord) => {
	const VOWELS = ["a", "e", "i", "o", "u", "A", "E", "I", "O", "U"];
	const num = Number(numberOfElements);

	if (numberOfElements === null || Number.isNaN(num)) {
		throw new Error("A valid number must be provided");
	}

	if (num === 1) {
		if (VOWELS.includes(firstLetterOfNextWord)) return "an";
		else return "a";
	}

	return num.toString();
};

const extractFirstWordsFromText = (text, n) => {
	if (typeof text !== "string" || typeof n !== "number" || n < 1) {
		return;
	}

	const textArray = text.split(" ");
	if (textArray.length <= n) return text;

	for (let index = textArray.length; index > n; index--) {
		textArray.pop();
	}

	return textArray.join(" ");
};

const getSaleStatusColor = status => {
	switch (status) {
		case "Pending":
			return "text-orange-400";

		case "Fulfilled":
			return "text-green-400";

		case "Cancelled":
			return "text-red-400";
		default:
			break;
	}
};

const addParamsToURL = (baseUrl, params = {}) => {
	if (typeof params !== "object" || params === null) {
		return baseUrl;
	}
	let url = baseUrl;
	const paramList = Object.keys(params);

	if (paramList.length > 0) {
		url += "?";
		paramList.forEach(param => {
			url += `${param}=${params[param]}&`;
		});
		url = url.slice(0, -1);
	}
	return url;
};

export {
	capitalizeFirstLetter,
	getArticleOfSentence,
	extractFirstWordsFromText,
	getSaleStatusColor,
	addParamsToURL,
};
