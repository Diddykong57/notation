jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GetService } from '../service/get.service';
import { IGet, Get } from '../get.model';
import { IAssessment } from 'app/entities/assessment/assessment.model';
import { AssessmentService } from 'app/entities/assessment/service/assessment.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

import { GetUpdateComponent } from './get-update.component';

describe('Component Tests', () => {
  describe('Get Management Update Component', () => {
    let comp: GetUpdateComponent;
    let fixture: ComponentFixture<GetUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let getService: GetService;
    let assessmentService: AssessmentService;
    let studentService: StudentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GetUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GetUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      getService = TestBed.inject(GetService);
      assessmentService = TestBed.inject(AssessmentService);
      studentService = TestBed.inject(StudentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Assessment query and add missing value', () => {
        const get: IGet = { id: 456 };
        const assessment: IAssessment = { id: 86828 };
        get.assessment = assessment;

        const assessmentCollection: IAssessment[] = [{ id: 20139 }];
        jest.spyOn(assessmentService, 'query').mockReturnValue(of(new HttpResponse({ body: assessmentCollection })));
        const additionalAssessments = [assessment];
        const expectedCollection: IAssessment[] = [...additionalAssessments, ...assessmentCollection];
        jest.spyOn(assessmentService, 'addAssessmentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ get });
        comp.ngOnInit();

        expect(assessmentService.query).toHaveBeenCalled();
        expect(assessmentService.addAssessmentToCollectionIfMissing).toHaveBeenCalledWith(assessmentCollection, ...additionalAssessments);
        expect(comp.assessmentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Student query and add missing value', () => {
        const get: IGet = { id: 456 };
        const student: IStudent = { id: 5596 };
        get.student = student;

        const studentCollection: IStudent[] = [{ id: 58026 }];
        jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
        const additionalStudents = [student];
        const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
        jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ get });
        comp.ngOnInit();

        expect(studentService.query).toHaveBeenCalled();
        expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(studentCollection, ...additionalStudents);
        expect(comp.studentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const get: IGet = { id: 456 };
        const assessment: IAssessment = { id: 75336 };
        get.assessment = assessment;
        const student: IStudent = { id: 41407 };
        get.student = student;

        activatedRoute.data = of({ get });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(get));
        expect(comp.assessmentsSharedCollection).toContain(assessment);
        expect(comp.studentsSharedCollection).toContain(student);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Get>>();
        const get = { id: 123 };
        jest.spyOn(getService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ get });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: get }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(getService.update).toHaveBeenCalledWith(get);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Get>>();
        const get = new Get();
        jest.spyOn(getService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ get });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: get }));
        saveSubject.complete();

        // THEN
        expect(getService.create).toHaveBeenCalledWith(get);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Get>>();
        const get = { id: 123 };
        jest.spyOn(getService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ get });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(getService.update).toHaveBeenCalledWith(get);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAssessmentById', () => {
        it('Should return tracked Assessment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAssessmentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackStudentById', () => {
        it('Should return tracked Student primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackStudentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
