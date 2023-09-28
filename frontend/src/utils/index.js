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

export { capitalizeFirstLetter, getArticleOfSentence };
