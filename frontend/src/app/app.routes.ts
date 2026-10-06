import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { RegisterComponent } from './features/auth/pages/register/register.component';
import { DashboardComponent } from './features/dashboard/pages/dashboard/dashboard.component';
import { authGuard } from './core/guards/auth.guard';
import { ProductsComponent } from './features/products/pages/products/products.component';
import { CategoriesComponent } from './features/categories/pages/categories/categories.component';
import { SuppliersComponent } from './features/suppliers/pages/suppliers/suppliers.component';
import { RolesComponent } from './features/roles/pages/roles/roles.component';
import { UsersComponent } from './features/users/pages/users/users.component';
import { PermissionsComponent } from './features/permissions/pages/permissions/permissions.component';

export const routes: Routes = [
    {
        path: "login",
        component: LoginComponent
    },
    {
        path: "register",
        component: RegisterComponent
    },
    {
        path: "dashboard",
        component: DashboardComponent,
        canActivate: [authGuard],
        children: [
            {
                path: "products",
                component: ProductsComponent
            },
            {
                path: "categories",
                component: CategoriesComponent
            },
            {
                path: "suppliers",
                component: SuppliersComponent
            },
            {
                path: "roles",
                component: RolesComponent
            },
            {
                path: "users",
                component: UsersComponent
            },
            {
                path: "permissions",
                component: PermissionsComponent
            }
        ]
    },
    {
        path: "",
        redirectTo: "dashboard",
        pathMatch: "full"
    },
];
