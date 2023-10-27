import { afterEach, beforeEach, describe, expect, test, vi } from "vitest";
import useNotification from "@/composables/useNotification";

const { showMsg, msgColor, displayMsg } = useNotification();

describe("useNotification", () => {
	beforeEach(() => {
		vi.useFakeTimers();
	});

	afterEach(() => {
		vi.useRealTimers();
	});

	test("displayMsg should change showMsg, and msgColor", () => {
		expect(showMsg.value).toBeNull();
		expect(msgColor.value).toBeUndefined();
		displayMsg("Foo", "green");
		expect(showMsg.value).toBe("Foo");
		expect(msgColor.value).toBe("green");
	});

	test("displayMsg shold rever showMsg, and msgColor after 5 seconds", () => {
		displayMsg("Foo", "green");
		expect(showMsg.value).toBe("Foo");
		expect(msgColor.value).toBe("green");

		vi.advanceTimersByTime(5000);

		expect(showMsg.value).toBeNull();
		expect(msgColor.value).toBeUndefined();
	});
});
