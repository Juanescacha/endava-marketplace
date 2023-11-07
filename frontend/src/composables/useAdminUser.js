import { ref } from "vue";
import { isLogedUserAdmin } from "@/utils/axios";

export default function useAdminUser() {
	const userIsAdmin = ref(false);

	const setUserIsAdmin = async () =>
		new Promise(resolve => {
			isLogedUserAdmin().then(response => {
				if (
					response.hasOwnProperty("data") &&
					typeof response.data === "boolean"
				) {
					userIsAdmin.value = response.data;
				}
				resolve();
			});
		});

	return { userIsAdmin, setUserIsAdmin };
}
