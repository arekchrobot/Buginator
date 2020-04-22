import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ApplicationDetails} from "../model/application.model";
import {SessionService} from "../../shared/service/session.service";
import {PageableComponent} from "../../shared/component/pageable.component";
import {ActivatedRoute} from "@angular/router";
import {ApplicationService} from "../application.service";
import {ToastrService} from "ngx-toastr";
import {Chart} from 'chart.js';
import {TranslateService} from "@ngx-translate/core";
import {LastWeekErrors} from "../model/report.model";

@Component({
  selector: 'app-application-details',
  templateUrl: './application-details.component.html',
  styleUrls: ['./application-details.component.css']
})
export class ApplicationDetailsComponent extends PageableComponent implements OnInit {

  application: ApplicationDetails;
  chart = [];
  @ViewChild('lastWeekErrors')
  private lastWeekErrorsCanvas: ElementRef;
  public context: CanvasRenderingContext2D;

  constructor(private route: ActivatedRoute, private sessionService: SessionService,
              private applicationService: ApplicationService, private toastr: ToastrService,
              private translateService: TranslateService) {
    super();
  }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      let applicationId = parseInt(paramMap.get('id'));
      this.fetchApplicationDetails(applicationId);
      this.generateChartData(applicationId);
    });
  }

  private fetchApplicationDetails(applicationId: number) {
    this.applicationService.getApplicationDetails(applicationId)
      .then(app => this.application = app,
        error => this.toastr.error(error.message, 'Error'));
  }

  get canManageAggregators(): boolean {
    return this.sessionService.hasPermission("app_show_notification");
  }

  get canManageUsers(): boolean {
    return this.sessionService.hasPermission("app_manage_users");
  }

  private generateChartData(applicationId: number) {
    this.applicationService.getLastWeekErrorsReport(applicationId)
      .then((lastWeekErrors: LastWeekErrors) => {
        this.translateService.get(['application.details.chart.title', 'application.details.chart.datasetLabel'])
          .subscribe((translations => {
            let title = translations["application.details.chart.title"];
            let datasetLabel = translations["application.details.chart.datasetLabel"];
            this.context = (<HTMLCanvasElement>this.lastWeekErrorsCanvas.nativeElement).getContext('2d');
            let maxValue = Math.max.apply(Math, lastWeekErrors.data);
            this.chart = new Chart(this.context, {
              type: 'line',
              data: {
                labels: lastWeekErrors.labels,
                datasets: [
                  {
                    label: datasetLabel,
                    data: lastWeekErrors.data
                  }
                ]
              },
              options: {
                legend: {
                  display: false
                },
                scales: {
                  yAxes: [{
                    ticks: {
                      suggestedMin: 0,
                      stepValue: 1,
                      max: maxValue + 1
                    },
                    type: 'linear',
                    display: true,
                    position: 'left'
                  }],
                },
                title: {
                  display: true,
                  text: title
                }
              }
            });
          }));
      }, error => this.toastr.error(error.message, 'Error'));
  }
}
