function drawGraphics( x, y )
%DRAWGRAPHICS �˴���ʾ�йش˺�����ժҪ
%  function drawGraphics( x, y, num_monitor ): draw graphics by x, y
%
% Input:
%       x: xAxis of the graphics
%       y: yAxis of the graphics
%       num_monitor: the numbers of monitors
%
% Output:
%       completed graphics

plot(x, y, 'r');
title('Critical Path with different number of monitors');
xlabel('number of monitors');
ylabel('average response time');
legend('Critical Path');
end

