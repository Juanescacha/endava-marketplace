<script setup>
	import { onBeforeMount } from "vue";
	import { RouterLink } from "vue-router";
	import { getProfileImage } from "../utils/axios";
	import { useUserStore } from "@/stores/user";
	import ListBox from "./Inputs/ListBox.vue";
	import SearchBarInput from "./Inputs/SearchBarInput.vue";
	import LinkListItem from "@/components/Menus/LinkListItem.vue";

	import {
		BellIcon,
		MagnifyingGlassIcon,
		BookOpenIcon,
		ListBulletIcon,
		UserIcon,
		CurrencyDollarIcon,
		ArrowLeftOnRectangleIcon,
		ShoppingBagIcon,
		AdjustmentsHorizontalIcon,
	} from "@heroicons/vue/24/outline";

	import { CreditCardIcon } from "@heroicons/vue/24/solid";

	const user = useUserStore();

	onBeforeMount(async () => {
		const response = await getProfileImage();
		if (response.error) {
			// error
		} else {
			user.image = response.data;
		}
	});
</script>

<template>
	<header
		class="sticky top-0 z-50 flex w-full flex-wrap border-b bg-white/90 py-2.5 text-sm backdrop-blur-md sm:flex-nowrap sm:justify-start sm:py-4"
	>
		<nav
			class="mx-auto flex w-full max-w-7xl basis-full items-center px-4 sm:px-6 lg:px-8"
			aria-label="Global"
		>
			<div class="mr-5 md:mr-8">
				<router-link
					class="flex-none text-xl font-semibold focus:outline-offset-8 focus:outline-blue-500"
					to="/"
					aria-label="Brand"
					><img
						src="../assets/endava-logo.png"
						alt="logo"
						class="w-32"
				/></router-link>
			</div>
			<div
				class="ml-auto flex w-full items-center justify-end sm:order-3 sm:justify-end sm:gap-x-3"
			>
				<div class="sm:hidden">
					<button
						type="button"
						class="mr-2 inline-flex h-[2.375rem] w-[2.375rem] flex-shrink-0 items-center justify-center gap-2 rounded-full bg-white align-middle text-xs font-medium text-gray-700 transition-all hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-endava-400 focus:ring-offset-2 focus:ring-offset-white"
					>
						<MagnifyingGlassIcon class="h-5 w-5 text-current" />
					</button>
				</div>
				<div class="hidden sm:block">
					<label
						for="icon"
						class="sr-only"
						>Search</label
					>
					<div class="relative">
						<div
							class="pointer-events-none absolute inset-y-0 left-0 z-20 flex items-center pl-4"
						>
							<MagnifyingGlassIcon
								class="h-5 w-5 text-gray-400"
							/>
						</div>
						<SearchBarInput />
					</div>
				</div>
				<ListBox />
				<div></div>
				<div class="flex gap-4">
					<router-link
						to="/listings/new"
						class="endava inline-flex items-center justify-center gap-2 rounded-md border px-4 py-3 text-sm font-semibold text-white shadow-sm transition-all focus:outline-none focus:ring-2 focus:ring-endava-600 focus:ring-offset-2 focus:ring-offset-white"
					>
						Sell
						<CreditCardIcon class="h-5 w-5" />
					</router-link>
				</div>
				<div class="flex flex-row items-center justify-end gap-2">
					<button
						type="button"
						title="Notifications"
						class="inline-flex h-[2.375rem] w-[2.375rem] flex-shrink-0 items-center justify-center gap-2 rounded-full bg-white/5 align-middle text-xs font-medium text-gray-700 hover:bg-gray-100/50 hover:shadow-md focus:outline-blue-500 active:bg-gray-200/50"
					>
						<BellIcon class="h-5 w-5 text-current" />
					</button>
					<router-link
						to="/users/me/publications"
						title="Listings"
						class="inline-flex h-[2.375rem] w-[2.375rem] flex-shrink-0 items-center justify-center gap-2 rounded-full bg-white/5 align-middle text-xs font-medium text-gray-700 hover:bg-gray-100/50 hover:shadow-md focus:outline-blue-500 active:bg-gray-200/50"
						data-hs-offcanvas="#hs-offcanvas-right"
					>
						<BookOpenIcon class="h-5 w-5 text-current" />
					</router-link>

					<div
						class="hs-dropdown relative inline-flex"
						data-hs-dropdown-placement="bottom-right"
					>
						<button
							id="hs-dropdown-with-header"
							type="button"
							class="hs-dropdown-toggle inline-flex h-[2.375rem] w-[2.375rem] flex-shrink-0 items-center justify-center gap-2 rounded-full bg-white align-middle text-xs font-medium text-gray-700 transition-all hover:bg-gray-50 hover:shadow-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 focus:ring-offset-white active:brightness-75"
						>
							<img
								class="inline-block h-[2.375rem] w-[2.375rem] rounded-full"
								:src="user.image"
								alt="Image Description"
							/>
						</button>

						<div
							class="hs-dropdown-menu duration z-10 hidden min-w-[15rem] rounded-lg bg-white p-2 opacity-0 shadow-xl transition-[opacity,margin] hs-dropdown-open:opacity-100"
							aria-labelledby="hs-dropdown-with-header"
						>
							<div
								class="-m-2 rounded-t-lg bg-gray-100 px-5 py-3"
							>
								<p class="text-sm text-gray-500">
									Signed in as
								</p>
								<p class="text-sm font-medium text-gray-800">
									{{ user.name }}
								</p>
							</div>
							<ul class="mt-2 py-2 first:pt-0 last:pb-0">
								<li>
									<link-list-item redirects-to="">
										<BellIcon
											class="h-5 w-5 flex-none text-current"
										/>
										Notifications
									</link-list-item>
								</li>
								<li>
									<link-list-item
										redirects-to="/users/me/profile"
									>
										<UserIcon class="h-5 w-5 flex-none" />
										Profile
									</link-list-item>
								</li>
								<li>
									<link-list-item
										redirects-to="/users/me/publications"
									>
										<BookOpenIcon
											class="h-5 w-5 flex-none text-current"
										/>
										Publications
									</link-list-item>
								</li>
								<li>
									<link-list-item
										redirects-to="/users/me/sales-history"
									>
										<CurrencyDollarIcon
											class="h-5 w-5 flex-none text-current"
										/>
										Sales
									</link-list-item>
								</li>
								<li>
									<link-list-item
										redirects-to="/users/me/purchase-history"
									>
										<ShoppingBagIcon
											class="h-5 w-5 flex-none text-current"
										/>
										Purchases
									</link-list-item>
								</li>
								<li>
									<link-list-item
										redirects-to="/admin-panel/general"
										v-if="user.isAdmin"
									>
										<AdjustmentsHorizontalIcon
											class="h-5 w-5 flex-none text-current"
										/>
										Dashboard
									</link-list-item>
									<link-list-item redirects-to="/logout">
										<ArrowLeftOnRectangleIcon
											class="h-5 w-5 flex-none text-current"
										/>
										Logout
									</link-list-item>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</nav>
	</header>
</template>
