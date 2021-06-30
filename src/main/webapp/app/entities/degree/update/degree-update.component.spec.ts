jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DegreeService } from '../service/degree.service';
import { IDegree, Degree } from '../degree.model';

import { DegreeUpdateComponent } from './degree-update.component';

describe('Component Tests', () => {
  describe('Degree Management Update Component', () => {
    let comp: DegreeUpdateComponent;
    let fixture: ComponentFixture<DegreeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let degreeService: DegreeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DegreeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DegreeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DegreeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      degreeService = TestBed.inject(DegreeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const degree: IDegree = { id: 456 };

        activatedRoute.data = of({ degree });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(degree));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Degree>>();
        const degree = { id: 123 };
        jest.spyOn(degreeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ degree });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: degree }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(degreeService.update).toHaveBeenCalledWith(degree);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Degree>>();
        const degree = new Degree();
        jest.spyOn(degreeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ degree });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: degree }));
        saveSubject.complete();

        // THEN
        expect(degreeService.create).toHaveBeenCalledWith(degree);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Degree>>();
        const degree = { id: 123 };
        jest.spyOn(degreeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ degree });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(degreeService.update).toHaveBeenCalledWith(degree);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
