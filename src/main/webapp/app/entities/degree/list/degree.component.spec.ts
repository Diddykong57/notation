import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DegreeService } from '../service/degree.service';

import { DegreeComponent } from './degree.component';

describe('Component Tests', () => {
  describe('Degree Management Component', () => {
    let comp: DegreeComponent;
    let fixture: ComponentFixture<DegreeComponent>;
    let service: DegreeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DegreeComponent],
      })
        .overrideTemplate(DegreeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DegreeComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DegreeService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.degrees?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
