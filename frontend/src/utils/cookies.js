const createCookie = ({ key, value, expiration }) => {
	let newCookie = `${key}=${value};SameSite=Strict`;

	if (expiration) {
		newCookie = newCookie.concat(`;expires=${expiration}`);
	}
	document.cookie = newCookie;
};

const getCookie = name => {
	const cookies = {};
	const cookieList = document.cookie.split(";");
	cookieList.forEach(cookie => {
		const [key, value] = cookie.split("=");
		cookies[key.trim()] = value;
	});
	return cookies[name];
};

const deleteCookie = name => {
	createCookie({
		key: name,
		value: "",
		expiration: "Thu, 01 Jan 1970 00:00:00 UTC",
	});
};

export { createCookie, getCookie, deleteCookie };
