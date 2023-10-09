const capitalizeFirstLetter = word => {
	if (!Number.isNaN(Number(word))) {
		return word;
	}
	if (typeof word !== "string" || word.length < 2) {
		throw new Error("Not a valid word to capitalize");
	}
	let firstLetter = word.charAt(0);
	const remainingLetters = word.slice(1);

	firstLetter = firstLetter.toUpperCase();
	return firstLetter + remainingLetters;
};

const getArticleOfSentence = (number, nextLetter) => {
	const VOWELS = ["a", "e", "i", "o", "u"];
	const num = Number(number);

	if (num === 1) {
		if (VOWELS.includes(nextLetter)) return "an";
		else return "a";
	}

	return num.toString();
};

const trimTextToLength = (text, length) => {
	if (typeof text !== "string" || typeof length !== "number" || length < 1) {
		return;
	}
	if (text.length < length) return text;

	const index = length + 1;
	const shortText = text.slice(0, index);
	const shortTextArray = shortText.split(" ");
	let lastWord = shortTextArray[shortTextArray.length - 1];

	if (lastWord === "") {
		shortTextArray.pop();
		lastWord = shortTextArray[shortTextArray.length - 1];
	}

	if (lastWord.length <= 3) {
		shortTextArray.pop();
	} else {
		shortTextArray[shortTextArray.length - 1] = lastWord.slice(0, -3);
	}

	return `${shortTextArray.join(" ")}...`;
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

		case "Canceled":
			return "text-red-400";
		default:
			break;
	}
};

export {
	capitalizeFirstLetter,
	getArticleOfSentence,
	trimTextToLength,
	extractFirstWordsFromText,
	getSaleStatusColor,
};
