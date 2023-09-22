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

export { capitalizeFirstLetter };
