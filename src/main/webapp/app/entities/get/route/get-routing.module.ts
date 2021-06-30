import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GetComponent } from '../list/get.component';
import { GetDetailComponent } from '../detail/get-detail.component';
import { GetUpdateComponent } from '../update/get-update.component';
import { GetRoutingResolveService } from './get-routing-resolve.service';

const getRoute: Routes = [
  {
    path: '',
    component: GetComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GetDetailComponent,
    resolve: {
      get: GetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GetUpdateComponent,
    resolve: {
      get: GetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GetUpdateComponent,
    resolve: {
      get: GetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(getRoute)],
  exports: [RouterModule],
})
export class GetRoutingModule {}
