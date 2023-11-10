import { ref } from "vue";
import { getUsers } from "@/utils/axios";

export default function useUserList() {
	const users = ref([]);
	const currentPage = ref(1);
	const filters = ref({});

	const fetchUsers = () => {
		getUsers(currentPage.value, filters.value).then(response => {
			if (response.data?.content) {
				users.value = response.data.content;
			}
		});
	};

	function addToCurrentPageValue(value) {
		if (typeof value !== "number" || currentPage.value + value < 1) {
			return;
		}
		currentPage.value += value;
	}

	const changePage = value => {
		addToCurrentPageValue(value);
		fetchUsers();
	};

	const updateFilters = (value = {}) => {
		if (
			typeof value !== "object" ||
			value === null ||
			Array.isArray(value)
		) {
			return;
		}
		filters.value = value;
	};

	return {
		users,
		currentPage,
		filters,
		fetchUsers,
		changePage,
		updateFilters,
		addToCurrentPageValue,
	};
}
