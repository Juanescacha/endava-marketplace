<script setup>
	import { onBeforeMount } from "vue";
	import { useRoute, useRouter } from "vue-router";
	import LinkListItem from "@/components/Menus/LinkListItem.vue";
	import { useUserStore } from "@/stores/user";

	const route = useRoute();
	const router = useRouter();

	const user = useUserStore();

	onBeforeMount(() => {
		if (!user.isAdmin) {
			router.push("/");
		}
	});

	const getColorsForLi = pageName => {
		if (typeof pageName === "string" && route.path.includes(pageName)) {
			return "text-white bg-endava-400";
		}
		return "text-gray-800 bg-gray-200 hover:bg-gray-300 active:bg-gray-400";
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
		<ul class="mx-10 grid grid-cols-3 gap-3">
			<li
				v-for="page in PAGES"
				:key="page.name"
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
