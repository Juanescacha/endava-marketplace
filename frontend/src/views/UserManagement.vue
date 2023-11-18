<script setup>
	import { onBeforeMount } from "vue";
	import {
		Disclosure,
		DisclosureButton,
		DisclosurePanel,
	} from "@headlessui/vue";
	import { ChevronUpIcon } from "@heroicons/vue/20/solid";
	import { patchAdminRole } from "@/utils/axios";
	import useUserList from "@/composables/useUserList";
	import TableBase from "@/components/TableBase.vue";
	import FilterForm from "@/components/FilterForm.vue";

	const columns = ["Name", "Email", "Administrator"];
	const { users, currentPage, fetchUsers, changePage, updateFilters } =
		useUserList();

	onBeforeMount(() => fetchUsers());

	const handleAdminToogle = ($event, user) => {
		patchAdminRole(user.id, $event.target.checked).then(result => {
			if (result.error) {
				const checkbox = document.getElementById(
					`${user.email}-admin-checkbox`
				);
				user.admin = !$event.target.checked;
				if (checkbox) checkbox.checked = !$event.target.checked;
			}
		});
	};

	const handleFilteredSearch = payload => {
		const cleanedFilters = {};
		if (payload.name.length > 0) {
			cleanedFilters.name = payload.name;
		}
		if (payload.email.length > 0) {
			cleanedFilters.email = payload.email;
		}
		updateFilters(cleanedFilters);
		currentPage.value = 1;
		fetchUsers();
	};
</script>

<template>
	<div class="mx-auto w-full max-w-4xl">
		<div class="my-6">
			<h2 class="mr-3 inline text-3xl">User managment</h2>
			<Disclosure v-slot="{ open: filtersOpen }">
				<DisclosureButton class="py-2">
					<ChevronUpIcon
						:class="
							filtersOpen
								? 'rotate-180 transform'
								: 'rotate-90 transform'
						"
						class="h-6 w-6"
					/>
				</DisclosureButton>
				<DisclosurePanel class="text-gray-500">
					<filter-form
						@filtered-search="handleFilteredSearch"
					></filter-form>
				</DisclosurePanel>
			</Disclosure>
		</div>
		<table-base :columns="columns">
			<tr
				v-for="user in users"
				:key="user.id"
			>
				<td class="px-4 py-2 font-normal">{{ user.name }}</td>
				<td class="px-4 py-2 font-normal">{{ user.email }}</td>
				<td class="px-4 py-2">
					<input
						type="checkbox"
						:id="`${user.email}-admin-checkbox`"
						:checked="user.admin"
						class="cursor-pointer"
						@click="handleAdminToogle($event, user)"
					/>
				</td>
			</tr>
		</table-base>
		<div
			class="mt-4 flex justify-center"
			id="paginationControls"
		>
			<button
				type="button"
				id="previousPage"
				class="mx-2"
				:disabled="currentPage === 1"
				@click="changePage(-1)"
			>
				<ChevronUpIcon class="h-5 w-5 -rotate-90 transform" />
			</button>
			{{ currentPage }}
			<button
				type="button"
				id="nextPage"
				class="mx-2"
				:disabled="users.length < 15"
				@click="changePage(1)"
			>
				<ChevronUpIcon class="h-5 w-5 rotate-90 transform" />
			</button>
		</div>
	</div>
</template>
