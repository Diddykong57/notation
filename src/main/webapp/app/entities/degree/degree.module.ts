import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DegreeComponent } from './list/degree.component';
import { DegreeDetailComponent } from './detail/degree-detail.component';
import { DegreeUpdateComponent } from './update/degree-update.component';
import { DegreeDeleteDialogComponent } from './delete/degree-delete-dialog.component';
import { DegreeRoutingModule } from './route/degree-routing.module';

@NgModule({
  imports: [SharedModule, DegreeRoutingModule],
  declarations: [DegreeComponent, DegreeDetailComponent, DegreeUpdateComponent, DegreeDeleteDialogComponent],
  entryComponents: [DegreeDeleteDialogComponent],
})
export class DegreeModule {}
