create database inventory_management;

create table categories (
    category_id bigint generated always as identity primary key,
    category_code varchar(10) not null unique,
    category_name varchar(60) not null,
    description varchar(250),
    status varchar(10) not null default 'ACTIVO',
    creation_date timestamp not null,
    modification_date timestamp not null
);

create table suppliers (
    supplier_id bigint generated always as identity primary key,
    supplier_code varchar(10) not null unique,
    document_type varchar(15) not null,
    document_number varchar(20) not null unique,
    business_name varchar(150) not null,
    contact_name varchar(100),
    phone varchar(15) not null,
    email varchar(100) not null unique,
    address varchar(200),
    status varchar(10) not null default 'ACTIVO',
    creation_date timestamp not null,
    modification_date timestamp not null
);

create table roles (
    role_id bigint generated always as identity primary key,
    role_name varchar(50) not null unique,
    description varchar(200),
    status varchar(10) not null default 'ACTIVO',
    creation_date timestamp not null,
    modification_date timestamp not null
);

create table permissions (
    permission_id bigint generated always as identity primary key,
    permission_code varchar(60) not null unique,
    permission_name varchar(100) not null,
    module varchar(30) not null,
    description varchar(200),
    status varchar(10) not null default 'ACTIVO',
    creation_date timestamp not null,
    modification_date timestamp not null
);

create table users (
    user_id bigint generated always as identity primary key,
    username varchar(30) not null unique,
    password varchar(255) not null,
    first_names varchar(60) not null,
    last_names varchar(60) not null,
    email varchar(100) not null unique,
    phone varchar(15),
    role_id bigint not null,
    status varchar(10) not null default 'ACTIVO',
    failed_attempts integer not null default 0,
    creation_date timestamp not null,
    modification_date timestamp not null,

    constraint fk_users_roles
        foreign key (role_id) references roles(role_id)
);

create table products (
    product_id bigint generated always as identity primary key,
    product_code varchar(20) not null unique,
    product_name varchar(120) not null,
    description varchar(500),
    category_id bigint not null,
    supplier_id bigint not null,
    unit_of_measure varchar(15) not null,
    purchase_price decimal(12,2) not null,
    sale_price decimal(12,2) not null,
    current_stock integer not null,
    minimum_stock integer not null,
    maximum_stock integer,
    status varchar(10) not null default 'ACTIVO',
    creation_date timestamp not null,
    modification_date timestamp not null,

    constraint fk_products_categories
        foreign key (category_id) references categories(category_id),

    constraint fk_products_suppliers
        foreign key (supplier_id) references suppliers(supplier_id)
);

create table stock_movements (
    movement_id bigint generated always as identity primary key,
    movement_type varchar(20) not null,
    product_id bigint not null,
    quantity integer not null,
    reference_document varchar(30),
    reason varchar(250),
    user_id bigint not null,
    movement_date timestamp not null,
    status varchar(10) not null default 'ACTIVO',
    modification_date timestamp not null,

    constraint fk_stock_movements_products
        foreign key (product_id) references products(product_id),

    constraint fk_stock_movements_users
        foreign key (user_id) references users(user_id)
);

create table role_permissions (
    role_id bigint not null,
    permission_id bigint not null,
    assigned_at timestamp not null,

    primary key (role_id, permission_id),

    constraint fk_role_permissions_roles
        foreign key (role_id) references roles(role_id),

    constraint fk_role_permissions_permissions
        foreign key (permission_id) references permissions(permission_id)
);

create table binnacle (
    log_id bigint generated always as identity primary key,
    username varchar(50) not null,
    date_time timestamp not null,
    ip_address varchar(45) not null,
    result varchar(10) not null
);

select * from users;