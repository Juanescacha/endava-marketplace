<script setup>
	import { onBeforeMount } from "vue";
	import { useRoute, useRouter } from "vue-router";
	import useAdminUser from "@/composables/useAdminUser";
	import LinkListItem from "@/components/Menus/LinkListItem.vue";

	const route = useRoute();
	const router = useRouter();
	const { userIsAdmin, setUserIsAdmin } = useAdminUser();

	onBeforeMount(async () => {
		await setUserIsAdmin();
		if (!userIsAdmin.value) {
			router.push("/");
		}
	});

	const getColorsForLi = pageName => {
		if (route.path.includes(pageName)) {
			return "text-white bg-endava-500";
		}
		return "text-gray-800 bg-gray-100 hover:bg-gray-200 active:bg-gray-300";
	};

	const PAGES = [
		{ name: "general", link: "/admin-panel/general" },
		{ name: "users", link: "/admin-panel/users" },
		{ name: "categories", link: "/admin-panel/categories" },
	];
</script>

<template>
	<h1 class="my-4 text-center">Admin panel</h1>
	<nav>
		<ul class="grid grid-cols-3">
			<li
				v-for="page in PAGES"
				:key="page.name"
				class="border-r-2"
			>
				<link-list-item
					:redirects-to="page.link"
					:color-classes="getColorsForLi(page.name)"
				>
					<span class="inline-block w-full text-center capitalize">
						{{ page.name }}
					</span>
				</link-list-item>
			</li>
		</ul>
	</nav>
	<main class="p-4">
		<router-view></router-view>
	</main>
</template>
