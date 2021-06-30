import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'student',
        data: { pageTitle: 'notationApp.student.home.title' },
        loadChildren: () => import('./student/student.module').then(m => m.StudentModule),
      },
      {
        path: 'degree',
        data: { pageTitle: 'notationApp.degree.home.title' },
        loadChildren: () => import('./degree/degree.module').then(m => m.DegreeModule),
      },
      {
        path: 'assessment',
        data: { pageTitle: 'notationApp.assessment.home.title' },
        loadChildren: () => import('./assessment/assessment.module').then(m => m.AssessmentModule),
      },
      {
        path: 'subject',
        data: { pageTitle: 'notationApp.subject.home.title' },
        loadChildren: () => import('./subject/subject.module').then(m => m.SubjectModule),
      },
      {
        path: 'get',
        data: { pageTitle: 'notationApp.get.home.title' },
        loadChildren: () => import('./get/get.module').then(m => m.GetModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
