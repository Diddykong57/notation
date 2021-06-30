import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DegreeComponent } from '../list/degree.component';
import { DegreeDetailComponent } from '../detail/degree-detail.component';
import { DegreeUpdateComponent } from '../update/degree-update.component';
import { DegreeRoutingResolveService } from './degree-routing-resolve.service';

const degreeRoute: Routes = [
  {
    path: '',
    component: DegreeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DegreeDetailComponent,
    resolve: {
      degree: DegreeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DegreeUpdateComponent,
    resolve: {
      degree: DegreeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DegreeUpdateComponent,
    resolve: {
      degree: DegreeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(degreeRoute)],
  exports: [RouterModule],
})
export class DegreeRoutingModule {}
