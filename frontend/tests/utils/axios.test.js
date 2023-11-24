import { afterEach, describe, expect, expectTypeOf, it, vi } from "vitest";
import axios from "axios";
import * as axiosUtils from "@/utils/axios";

const mockResponse = { status: 200, data: { foo: "" } };
afterEach(() => {
	vi.restoreAllMocks();
	mockResponse.status = 200;
});

describe("postNewListing", () => {
	const { postNewListing } = axiosUtils;
	const mockData = {
		name: "foo",
		bool: true,
		id: 1,
	};

	it("should call axios.post", async () => {
		const functionSpy = vi.spyOn(axios, "post");

		await postNewListing(mockData);
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should return a response and a msg on a 200 status response", async () => {
		vi.spyOn(axios, "post").mockImplementationOnce(() => mockResponse);

		const result = await postNewListing(mockData);
		expect(result).toHaveProperty("response");
		expect(result.response).toHaveProperty("data");
		expect(result.response.status).toBe(mockResponse.status);
		expect(result).toHaveProperty("msg");
		expect(result.msg).toContain("Succes");
	});

	it("should return a response and a msg on a 201 status response", async () => {
		mockResponse.status = 201;
		vi.spyOn(axios, "post").mockImplementationOnce(() => mockResponse);

		const result = await postNewListing(mockData);
		expect(result.response.status).toBe(mockResponse.status);
		expect(result.msg).toContain("Succes");
	});

	it("should return a failure msg on any status code different to 200 or 201", async () => {
		vi.spyOn(axios, "post").mockImplementation(() => mockResponse);

		mockResponse.status = 203;
		let result = await postNewListing(mockData);
		expect(result.msg).toContain("Fail");

		mockResponse.status = 400;
		result = await postNewListing(mockData);
		expect(result.msg).toContain("Fail");

		mockResponse.status = 500;
		result = await postNewListing(mockData);
		expect(result.msg).toContain("Fail");
	});

	it("should return an error when axios.post throws an exception", async () => {
		const errorMsg = "Connetion_ERR";
		vi.spyOn(axios, "post").mockImplementationOnce(() => {
			throw new Error(errorMsg);
		});

		const result = await postNewListing(mockData);
		expect(result).toHaveProperty("error");
		expect(result.error.toString()).toContain(errorMsg);
	});
});

describe("postImagesOfListing", () => {
	const { postImagesOfListing } = axiosUtils;

	it("should call axios.post", async () => {
		const functionSpy = vi.spyOn(axios, "post");

		await postImagesOfListing(0);
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should return an empty object on success", async () => {
		vi.spyOn(axios, "post").mockImplementationOnce(() => mockResponse);

		const result = await postImagesOfListing(0);
		expectTypeOf(result).toBeObject();
		expect(Object.keys(result).length).toBe(0);
	});

	it("should return an error if there is no response from axios", async () => {
		vi.spyOn(axios, "post").mockImplementationOnce(() => {});

		const result = await postImagesOfListing(0);
		expect(result).toHaveProperty("error");
		expect(result).toHaveProperty("msg");
	});

	it("should return an error the response code is not 200 or 201", async () => {
		vi.spyOn(axios, "post").mockImplementation(() => mockResponse);

		mockResponse.status = 203;
		let result = await postImagesOfListing(0);
		expect(result).toHaveProperty("error");

		mockResponse.status = 400;
		result = await postImagesOfListing(0);
		expect(result).toHaveProperty("error");

		mockResponse.status = 500;
		result = await postImagesOfListing(0);
		expect(result).toHaveProperty("error");
	});

	it("should return an error when axios.post throws an exception", async () => {
		const errorMsg = "Connetion_ERR";
		vi.spyOn(axios, "post").mockImplementationOnce(() => {
			throw new Error(errorMsg);
		});

		const result = await postImagesOfListing(0);
		expect(result).toHaveProperty("error");
		expect(result.error.toString()).toContain(errorMsg);
	});
});

describe("postSale", () => {
	const { postSale } = axiosUtils;

	it("should call axios.post", async () => {
		const functionSpy = vi.spyOn(axios, "post");

		await postSale({});
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should return an empty object on success", async () => {
		vi.spyOn(axios, "post").mockImplementationOnce(() => mockResponse);

		const result = await postSale({});
		expectTypeOf(result).toBeObject();
		expect(Object.keys(result).length).toBe(0);
	});

	it("should return an error if there is no response from axios", async () => {
		vi.spyOn(axios, "post").mockImplementationOnce(() => {});

		const result = await postSale({});
		expect(result).toHaveProperty("error");
		expect(result).toHaveProperty("msg");
	});

	it("should return an error the response code is not 200 or 201", async () => {
		vi.spyOn(axios, "post").mockImplementation(() => mockResponse);

		mockResponse.status = 203;
		let result = await postSale({});
		expect(result).toHaveProperty("error");

		mockResponse.status = 400;
		result = await postSale({});
		expect(result).toHaveProperty("error");

		mockResponse.status = 500;
		result = await postSale({});
		expect(result).toHaveProperty("error");
	});

	it("should return an error when axios.post throws an exception", async () => {
		const errorMsg = "Connetion_ERR";
		vi.spyOn(axios, "post").mockImplementationOnce(() => {
			throw new Error(errorMsg);
		});

		const result = await postSale({});
		expect(result).toHaveProperty("error");
		expect(result.error.toString()).toContain(errorMsg);
	});
});

describe("patchSaleStatus", () => {
	const { patchSaleStatus } = axiosUtils;

	it("should call axios.patch", async () => {
		const functionSpy = vi.spyOn(axios, "patch");

		await patchSaleStatus(0, "foo");
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should return an empty object on success", async () => {
		vi.spyOn(axios, "patch").mockImplementationOnce(() => mockResponse);

		const result = await patchSaleStatus(0, "foo");
		expectTypeOf(result).toBeObject();
		expect(Object.keys(result).length).toBe(0);
	});

	it("should return an error if there is no response from axios", async () => {
		vi.spyOn(axios, "patch").mockImplementationOnce(() => {});

		const result = await patchSaleStatus(0, "foo");
		expect(result).toHaveProperty("error");
		expect(result).toHaveProperty("msg");
	});

	it("should return an error the response code is not 200", async () => {
		vi.spyOn(axios, "patch").mockImplementation(() => mockResponse);

		mockResponse.status = 201;
		let result = await patchSaleStatus(0, "foo");
		expect(result).toHaveProperty("error");

		mockResponse.status = 400;
		result = await patchSaleStatus(0, "foo");
		expect(result).toHaveProperty("error");

		mockResponse.status = 500;
		result = await patchSaleStatus(0, "foo");
		expect(result).toHaveProperty("error");
	});

	it("should return an error when axios.patch throws an exception", async () => {
		const errorMsg = "Connetion_ERR";
		vi.spyOn(axios, "patch").mockImplementationOnce(() => {
			throw new Error(errorMsg);
		});

		const result = await patchSaleStatus(0, "foo");
		expect(result).toHaveProperty("error");
		expect(result.error.toString()).toContain(errorMsg);
	});
});

describe("deleteListing", () => {
	const { deleteListing } = axiosUtils;

	it("should call axios.delete", async () => {
		const functionSpy = vi.spyOn(axios, "delete");

		await deleteListing(0);
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should return an empty object on success", async () => {
		vi.spyOn(axios, "delete").mockImplementationOnce(() => mockResponse);

		const result = await deleteListing(0);
		expectTypeOf(result).toBeObject();
		expect(Object.keys(result).length).toBe(0);
	});

	it("should return an error if there is no response from axios", async () => {
		vi.spyOn(axios, "delete").mockImplementationOnce(() => {});

		const result = await deleteListing(0);
		expect(result).toHaveProperty("error");
		expect(result).toHaveProperty("msg");
	});

	it("should return an error the response code is not 200", async () => {
		vi.spyOn(axios, "delete").mockImplementation(() => mockResponse);

		mockResponse.status = 201;
		let result = await deleteListing(0);
		expect(result).toHaveProperty("error");

		mockResponse.status = 400;
		result = await deleteListing(0);
		expect(result).toHaveProperty("error");

		mockResponse.status = 500;
		result = await deleteListing(0);
		expect(result).toHaveProperty("error");
	});

	it("should return an error when axios.delete throws an exception", async () => {
		const errorMsg = "Connetion_ERR";
		vi.spyOn(axios, "delete").mockImplementationOnce(() => {
			throw new Error(errorMsg);
		});

		const result = await deleteListing(0);
		expect(result).toHaveProperty("error");
		expect(result.error.toString()).toContain(errorMsg);
	});
});

describe("postUser", () => {
	const { postUser } = axiosUtils;

	it("should call axios.post", async () => {
		const functionSpy = vi.spyOn(axios, "post");

		await postUser();
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should return an object with a data property on success", async () => {
		vi.spyOn(axios, "post").mockImplementationOnce(() => mockResponse);

		const result = await postUser();
		expect(result).toHaveProperty("foo");
		expect(result.foo).toBe(mockResponse.data.foo);
	});

	it("should return an error if there is no response from axios", async () => {
		vi.spyOn(axios, "post").mockImplementationOnce(() => {});

		const result = await postUser();
		expect(result).toHaveProperty("error");
	});

	it("should return an error when axios.post throws an exception", async () => {
		const errorMsg = "Connetion_ERR";
		vi.spyOn(axios, "post").mockImplementationOnce(() => {
			throw new Error(errorMsg);
		});

		const result = await postUser();
		expect(result).toHaveProperty("error");
		expect(result.error.toString()).toContain(errorMsg);
	});
});

describe("getSellerSales", () => {
	const { getSellerSales } = axiosUtils;

	it("should call axios.get", async () => {
		const functionSpy = vi.spyOn(axios, "get");

		await getSellerSales(0);
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should return an object with a data property on success", async () => {
		vi.spyOn(axios, "get").mockImplementationOnce(() => mockResponse);

		const result = await getSellerSales(0);
		expect(result).toHaveProperty("data");
		expect(result.data.foo).toBe(mockResponse.data.foo);
	});

	it("should return an error if there is no response from axios", async () => {
		vi.spyOn(axios, "get").mockImplementationOnce(() => {});

		const result = await getSellerSales(0);
		expect(result).toHaveProperty("error");
		expect(result).toHaveProperty("msg");
	});

	it("should return an error the response code is not 200", async () => {
		vi.spyOn(axios, "get").mockImplementation(() => mockResponse);

		mockResponse.status = 201;
		let result = await getSellerSales(0);
		expect(result).toHaveProperty("error");

		mockResponse.status = 400;
		result = await getSellerSales(0);
		expect(result).toHaveProperty("error");

		mockResponse.status = 500;
		result = await getSellerSales(0);
		expect(result).toHaveProperty("error");
	});

	it("should return an error when axios.get throws an exception", async () => {
		const errorMsg = "Connetion_ERR";
		vi.spyOn(axios, "get").mockImplementationOnce(() => {
			throw new Error(errorMsg);
		});

		const result = await getSellerSales(0);
		expect(result).toHaveProperty("error");
		expect(result.error.toString()).toContain(errorMsg);
	});
});

describe("getUserPurchases", () => {
	const { getUserPurchases } = axiosUtils;

	it("should call axios.get", async () => {
		const functionSpy = vi.spyOn(axios, "get");

		await getUserPurchases(0);
		expect(functionSpy).toHaveBeenCalled();
	});

	it("should return an object with a data property on success", async () => {
		vi.spyOn(axios, "get").mockImplementationOnce(() => mockResponse);

		const result = await getUserPurchases(0);
		expect(result).toHaveProperty("data");
		expect(result.data.foo).toBe(mockResponse.data.foo);
	});

	it("should return an error if there is no response from axios", async () => {
		vi.spyOn(axios, "get").mockImplementationOnce(() => {});

		const result = await getUserPurchases(0);
		expect(result).toHaveProperty("error");
		expect(result).toHaveProperty("msg");
	});

	it("should return an error the response code is not 200", async () => {
		vi.spyOn(axios, "get").mockImplementation(() => mockResponse);

		mockResponse.status = 201;
		let result = await getUserPurchases(0);
		expect(result).toHaveProperty("error");

		mockResponse.status = 400;
		result = await getUserPurchases(0);
		expect(result).toHaveProperty("error");

		mockResponse.status = 500;
		result = await getUserPurchases(0);
		expect(result).toHaveProperty("error");
	});

	it("should return an error when axios.get throws an exception", async () => {
		const errorMsg = "Connetion_ERR";
		vi.spyOn(axios, "get").mockImplementationOnce(() => {
			throw new Error(errorMsg);
		});

		const result = await getUserPurchases(0);
		expect(result).toHaveProperty("error");
		expect(result.error.toString()).toContain(errorMsg);
	});
});
