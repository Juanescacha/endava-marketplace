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

export { createCookie, getCookie };
