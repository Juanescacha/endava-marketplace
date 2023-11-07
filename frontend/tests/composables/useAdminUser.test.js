import { describe, expect, it, vi } from "vitest";
import * as axios from "@/utils/axios";
import useAdminUser from "@/composables/useAdminUser";

const { userIsAdmin, setUserIsAdmin } = useAdminUser();

const mockImplementationFactory = (property, value) => () =>
	new Promise(resolve => {
		const obj = {};
		obj[property] = value;
		resolve(obj);
	});

describe("setUserIsAdmin function", () => {
	it("should call isLogedUserAdmin", () => {
		const axiosSpy = vi.spyOn(axios, "isLogedUserAdmin");
		setUserIsAdmin();
		expect(axiosSpy).toHaveBeenCalled();
	});

	it("should change userIsAdmin to true when isLogedUserAdmin returns true", async () => {
		vi.spyOn(axios, "isLogedUserAdmin").mockImplementationOnce(
			mockImplementationFactory("data", true)
		);
		expect(userIsAdmin.value).toBe(false);
		await setUserIsAdmin();
		expect(userIsAdmin.value).toBe(true);
	});

	it("should change userIsAdmin to false when isLogedUserAdmin returns false", async () => {
		userIsAdmin.value = true;
		vi.spyOn(axios, "isLogedUserAdmin").mockImplementationOnce(
			mockImplementationFactory("data", false)
		);
		expect(userIsAdmin.value).toBe(true);
		await setUserIsAdmin();
		expect(userIsAdmin.value).toBe(false);
	});

	it("should leave userIsAdmin as false when isLogedUserAdmin doesn't return data", async () => {
		vi.spyOn(axios, "isLogedUserAdmin").mockImplementationOnce(
			mockImplementationFactory("error", "foo")
		);
		expect(userIsAdmin.value).toBe(false);
		await setUserIsAdmin();
		expect(userIsAdmin.value).toBe(false);
	});
});
