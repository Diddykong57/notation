jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AssessmentService } from '../service/assessment.service';
import { IAssessment, Assessment } from '../assessment.model';
import { ISubject } from 'app/entities/subject/subject.model';
import { SubjectService } from 'app/entities/subject/service/subject.service';

import { AssessmentUpdateComponent } from './assessment-update.component';

describe('Component Tests', () => {
  describe('Assessment Management Update Component', () => {
    let comp: AssessmentUpdateComponent;
    let fixture: ComponentFixture<AssessmentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let assessmentService: AssessmentService;
    let subjectService: SubjectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AssessmentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AssessmentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AssessmentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      assessmentService = TestBed.inject(AssessmentService);
      subjectService = TestBed.inject(SubjectService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Subject query and add missing value', () => {
        const assessment: IAssessment = { id: 456 };
        const subject: ISubject = { id: 71093 };
        assessment.subject = subject;

        const subjectCollection: ISubject[] = [{ id: 69163 }];
        jest.spyOn(subjectService, 'query').mockReturnValue(of(new HttpResponse({ body: subjectCollection })));
        const additionalSubjects = [subject];
        const expectedCollection: ISubject[] = [...additionalSubjects, ...subjectCollection];
        jest.spyOn(subjectService, 'addSubjectToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ assessment });
        comp.ngOnInit();

        expect(subjectService.query).toHaveBeenCalled();
        expect(subjectService.addSubjectToCollectionIfMissing).toHaveBeenCalledWith(subjectCollection, ...additionalSubjects);
        expect(comp.subjectsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const assessment: IAssessment = { id: 456 };
        const subject: ISubject = { id: 46367 };
        assessment.subject = subject;

        activatedRoute.data = of({ assessment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(assessment));
        expect(comp.subjectsSharedCollection).toContain(subject);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Assessment>>();
        const assessment = { id: 123 };
        jest.spyOn(assessmentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ assessment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: assessment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(assessmentService.update).toHaveBeenCalledWith(assessment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Assessment>>();
        const assessment = new Assessment();
        jest.spyOn(assessmentService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ assessment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: assessment }));
        saveSubject.complete();

        // THEN
        expect(assessmentService.create).toHaveBeenCalledWith(assessment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Assessment>>();
        const assessment = { id: 123 };
        jest.spyOn(assessmentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ assessment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(assessmentService.update).toHaveBeenCalledWith(assessment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSubjectById', () => {
        it('Should return tracked Subject primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSubjectById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
