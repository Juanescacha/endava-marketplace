export const mockImplementationFactory = (property, value) => () =>
	new Promise(resolve => {
		const obj = {};
		obj[property] = value;
		resolve(obj);
	});
